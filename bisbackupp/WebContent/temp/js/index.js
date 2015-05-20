// The root URL for the RESTful services
var rootLoginURL = "/choc/rest/api";
var loggedin = (localStorage.getItem('sid') != null);

// Register listeners
$('#loginBtn').click(function() {
	if(!loggedin) {
		console.log("Login Button Clicked");
		var uname = $('#username').val();
		var pass = $('#password').val();
		loginUser(uname, pass);
	} else {
		console.log("Logout Button Clicked");
		var cnf = confirm('Do you want to logout ' + localStorage.getItem("name"));
		if(cnf) {
			logoutUser();
		}
	}
	refresh();
	//location.reload();
	return false;
});


$('#signupBtn').click(function() {
	console.log("Signup Button Clicked");
	//signupUser($('#email_id').val(), $('#pass1').val(), $('#pass2').val(), $('#fname').val(), $('#lname').val(), $('#contact').val());
	//loadCat();
	//localStorage.clear();
	return false;
})


function refresh() {
	checkUserSession();
	if(localStorage.getItem("sid") == null) {
		console.log('no user logged in');
		document.getElementById("loginBtn").value="Login"; 
		$("#d1 #r1").text("Hello Guest");
	} else {
		console.log('logged in');
		document.getElementById("loginBtn").value="Logout"; 
		document.getElementById("r1").innerHTML = "Hello " + localStorage.getItem("name");
	}
}

function renderList(data) {
	
	console.log("renderList: Got data");
	console.log(data);
	if(data.status == 'success') {
		console.log('storing session');
		console.log(data.name)
		localStorage.setItem("sid", data.session_id);
		localStorage.setItem("name", data.name);	
	}
}



function storeSession(data) {
	console.log('storeSession: ');
	console.log(data);
	if(data.status == 'success') {
		console.log('storing session');
		console.log(data)
		localStorage.setItem("sid", data.session_id);
		localStorage.setItem("name", data.name);
		loggedin = true;
	}
}

function removeSession(data) {
	console.log('removeSession: ');
	console.log(data);
	if(data.status == 'success') {
		console.log('removing session');
		localStorage.removeItem("sid");
		localStorage.removeItem("name");	
		loggedin = true;
	}
}

function checkSession(data) {
	console.log('checkSession: ');
	console.log(data);
	if(data.exists == false) {
		console.log('removing session');
		localStorage.removeItem("sid");
		localStorage.removeItem("name");
		loggedin = true;
	}
}

function checkUserSession() {
	if(loggedin) {
		var item = {};
		item['sessionID'] = localStorage.getItem('sid');;
		var json = JSON.stringify(item);
		console.log('checking if session has expired');
		$.ajax({
			type: 'POST',
			url: rootLoginURL + '/checkSession',
			contentType : 'application/json',
			data : json,
			success : checkSession
		});
	} else {
		console.log('session does not exist');
	}
	
}

function loginUser(uname,pass) {
	var item={};	
	item["username"]=uname;
	item["password"]=pass;
	var credentials=JSON.stringify(item);
	console.log('credentials: ' + item['username'] + ' ' + item['password']);
	$.ajax({
		type: 'POST',
		url: rootLoginURL + '/login',
		contentType : 'application/json',
		data : credentials,
		success : storeSession
	});
}

function logoutUser() {
	var item = {};
	item['sessionID'] = localStorage.getItem('sid');
	var json = JSON.stringify(item);
	console.log('attempting to log out ' + localStorage.getItem('name'));
	$.ajax({
		type: 'POST',
		url: rootLoginURL + '/logout',
		contentType : 'application/json',
		data : json,
		success : removeSession
	});
}

function signupUser(email,pass1,pass2,fname,lname,contact) {
	if(pass1 == pass2) {
		var item = {};
		item['email_id']=email;
		item['password']=pass1;
		item['firstname']=fname;
		item['lastname']=lname;
		item['contact_no']=contact;
		var jsonObj = JSON.stringify(item);
		$.ajax({
			type  :'POST',
			url : rootLoginURL + '/signup',
			contentType : 'application/json',
			data : jsonObj,
			success  : renderList
		});
	}
	else {
		console.log('password doesnt match:' + pass1 + ' ' + pass2)
	}
	
}

