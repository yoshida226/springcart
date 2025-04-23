package com.example.springcart.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springcart.entity.Product;
import com.example.springcart.entity.Shop;
import com.example.springcart.entity.User;
import com.example.springcart.form.ProductUpdateForm;
import com.example.springcart.repository.ProductRepository;
import com.example.springcart.repository.UserShopRepository;
import com.example.springcart.util.AuthUtil;

@Service
public class AdminService {
	
	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	private UserShopRepository userShopRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	//ユーザが管理するショップを
	public List<Shop> getShopsByUser(Authentication auth) {
		User user = authUtil.getUserByAuth(auth);
		return userShopRepository.findShopsByUserId(user.getId());
	}
	
	
	/**
     * 画像ファイルを保存し、ファイル名を返す
     */
    public String saveImage(MultipartFile file) throws IOException {
        // オリジナルのファイル名を取得
        String originalFilename = file.getOriginalFilename();
        
        // ファイル名が重複しないようにUUIDを付与
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        
        // static/images フォルダへのパスを取得
        Path imagesDir = Paths.get("src/main/resources/static/images");
        
        // ディレクトリが存在しない場合は作成
        if (!Files.exists(imagesDir)) {
            Files.createDirectories(imagesDir);
        }
        
        // ファイルを指定ディレクトリに保存
        Path filePath = imagesDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }

	public void update(ProductUpdateForm form) {
		
		Product product = productRepository.findById(form.getId()).orElseThrow();
		product.setName(form.getName());
		product.setCategory(form.getCategory());
		product.setDescription(form.getDescription());
		product.setPrice(form.getPrice());
		product.setStock(form.getStock());
		
		if(form.getImage() != null) {
			product.setImage(form.getImage());
		}
		
		productRepository.save(product);
		
	}
}
