package com.example.springcart.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springcart.entity.Product;
import com.example.springcart.entity.Shop;
import com.example.springcart.entity.User;
import com.example.springcart.form.ProductConfirmForm;
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

    //商品を更新
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

	//商品を作成
	public void insert(ProductConfirmForm form) {
		Product product = new Product();
		product.setName(form.getName());
		product.setCategory(form.getCategory());
		product.setShop(form.getShop());
		product.setDescription(form.getDescription());
		product.setPrice(form.getPrice());
		product.setStock(form.getStock());
		
		if(form.getImage() != null) {
			product.setImage(form.getImage());
		}
		
		productRepository.save(product);
	}
	
	public MultipartFile convertBase64ToMultipartFile(String base64String, String fileName, String contentType) throws IOException {
	    // Base64のデータURL形式（data:image/jpeg;base64,/9j/4AAQ...）から実際のBase64部分を抽出
	    String base64Data = base64String;
	    if (base64String.contains(",")) {
	        base64Data = base64String.split(",")[1];
	    }
	    
	    // Base64デコード
	    byte[] fileContent = Base64.getDecoder().decode(base64Data);
	    
	    // MockMultipartFileを生成して返す
	    // パラメータ: フィールド名, ファイル名, ContentType, バイトデータ
	    return new MockMultipartFile(
	            "file",           // フォームのフィールド名
	            fileName,         // ファイル名 (例: "image.jpg")
	            contentType,      // Content Type (例: "image/jpeg")
	            fileContent       // ファイルの内容 (バイト配列)
	    );
	}
}
