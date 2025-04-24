const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

const stripe = window.Stripe('pk_test_51RCXMoQrrhKzyniq6Mr6r6ABOeb3cFj88M4cmYBxRs9Qmemkz85f2mbKZmOpf2ZW0OXpRlMyRG3b8GTLB3I5LVDT00gLLlyU5K');
document.getElementById("checkout-button").addEventListener("click", function () {
    fetch("/create-checkout-session", {
		method: "POST",
		headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({})
		})
        .then(response => response.json())
        .then(session => stripe.redirectToCheckout({ sessionId: session.id }));
});