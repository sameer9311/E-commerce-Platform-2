var rootProductURL = "/choc/rest/products";
var rootWishListURL = "/choc/rest/wishlist";


//Register listeners
$('#allProductBtn').click(function() {
	console.log("All Products Button Clicked");
	loadAllProducts();
	loadBrands();
	loadCat();
	return false;
});


$('#loadBtn').click(function() {
	console.log("Retrive Wishlist Button Clicked");
	checkUserSession();
	if(loggedin)
		getProductsFromWishlist();
	else {
		
	}
	return false;
})

$('#addBtn').click(function() {
	var pid = $('#pidTxt').val();
	console.log("call Add to Wishlist productID: " + pid);
	checkUserSession();
	if(loggedin) {
		addToWishlist(pid);
	} else {
		
	}
	return false;
})

$('#removeBtn').click(function() {
	var pid = $('#pidTxt').val();
	console.log("call remove from Wishlist productID: " + pid);
	checkUserSession();
	if(loggedin) {
		removeFromWishlist(pid);
	} else {
		
	}
	return false;
})

function loadAllProducts() {
	$.ajax({
		type: 'GET',
		url: rootProductURL + '/all',
		success : renderList2
	});
}

function loadBrands() {
	$.ajax({
		type: 'GET',
		url: rootProductURL + '/all_brands',
		success : renderList2
	});
}

function loadCat() {
	$.ajax({
		type: 'GET',
		url: rootProductURL + '/all_cat',
		success : renderList2
	});
}


function getProductsFromWishlist() {
	$.ajax({
		type: 'GET',
		url: rootWishListURL + '/view/' + localStorage.getItem('sid'),
		success : renderList2
	});
}

function addToWishlist(productID) {
	$.ajax({
		type: 'GET',
		url: rootWishListURL + '/add/' + localStorage.getItem('sid') + '/' + productID,
		success : displayJSON
	});
}

function removeFromWishlist(productID) {
	$.ajax({
		type: 'GET',
		url: rootWishListURL + '/remove/' + localStorage.getItem('sid') + '/' + productID,
		success : displayJSON
	});
}

function displayJSON(data) {
	console.log(data);
}

function renderList2(data) {
	console.log("Got JSON array:");
	for(i=0; i<data.length; i++) {
		console.log(data[i]);
	}
}