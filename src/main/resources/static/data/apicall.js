
tokenCheck();  

const apiUrl = 'http://localhost:9090/user';



function authenticate() {

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");


  var raw = JSON.stringify({
    "email": "pradip@inexture.com",
    "password": "123"
  });
  var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };
  
  fetch("http://localhost:9090/authenticate", requestOptions)
    .then(response => response.json())
    .then(response => {
      window.localStorage.setItem("token",'Bearer '+response.jwt);
      alert("Token generated");
    return true
    }) 
    .catch(error => {
      alert(error);
      return false;
    });
}

function tokenCheck(){
  var localToken=window.localStorage.getItem("token");
  if(!localToken){
    return authenticate();
  }
  return true;
}

function logout(){
  var localToken=window.localStorage.removeItem("token");
  alert("Logout Successfully...");
}


function getUsers() {
  if(tokenCheck()){
    var myHeaders = new Headers();
    myHeaders.append("Authorization", window.localStorage.getItem("token"));
    
    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };
    const table = $('.table');
    fetch("http://localhost:9090/user?projection=UserProjectionDTO", requestOptions)
      .then(result => result.json())
      .then(response => {
        if(response.statusCode == 200 && response.data != ''){
          // Iterate over the response data and add rows to the table
          response.data.forEach(user => {
            const row = `
              <tr>
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.enabled}</td>
                <td>${user.locked}</td>
              </tr>
            `;
            table.append(row);
          });
        }else{
          $('#msg').text(response.message);
          table.hide();
        }
      })
      .catch(error => {
        alert(error);
        console.log('error', error)
      });
  }  
  else{
    alert("Users is not fetch due to token");
  }
}

function createUser() {
    tokenCheck()
    var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", window.localStorage.getItem("token"));

    var raw = JSON.stringify({
    "email": document.getElementById("email").value,
    "fullName": document.getElementById("fullName").value,
    "password": document.getElementById("password").value,
    "enabled": true,
    "locked": false
    });
    console.log(raw)
    var requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
    };

    fetch('http://localhost:9090/user', requestOptions)
    .then(response => response.text())
    .then(result => console.log(result))
    .catch(error => console.log('error', error));

}

function takeConformation(){
  var result = confirm("Are you sure for delete user ?");
  if (!result) {
     event.preventDefault();
   }
   else{
     deleteUser();
   }
}

function deleteUser() {
    tokenCheck()
    var myHeaders = new Headers();
    myHeaders.append("Authorization", window.localStorage.getItem("token"));
    var requestOptions = {
      method: 'DELETE',
      headers: myHeaders,
      redirect: 'follow'
    };
    var userId=document.getElementById("userId").value;
    fetch("http://localhost:9090/user/"+userId, requestOptions)
      .then(response => response.json())
      .then(result => {
        alert(result.message);
        window.location.reload();
      })
      .catch(error => alert("Usern not deleted"));

}
