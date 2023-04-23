
function operations(type,id,element){
    if(type == "edit"){

    }
    if(type == "delete"){
        var myHeaders = new Headers();
        myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

        var requestOptions = {
          method: 'DELETE',
          headers: myHeaders,
        };

        fetch(backendServerUrl+"/user/expense/"+id, requestOptions)
          .then(response => response.json())
          .then(result => {
              if(result.status == 200 && result.data != ''){
                alert(result.message);
                window.location.href="expense-listing.html"
              }
              else{
                alert("Expense not deleted");
                window.location.reload();
              }
          })
          .catch(error => console.log('error', error));

    }
}

function getUsers() {
//    const myDiv = document.getElementById("user-table");
//    myDiv.style.display = "inline";
    var myHeaders = new Headers();
    myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };
    const table = $('.table tbody');
    fetch(backendServerUrl+"/user/expense", requestOptions)
      .then(result => result.json())
      .then(response => {

        if(response.status == 200 && response.data != ''){
console.log(response.data);
          // Iterate over the response data and add rows to the table
          response.data.forEach(function (expense, i) {
            const row = `
              <tr>
                <td>${i+1}</td>
                <td>${expense.user.id}</td>
                <td>${expense.user.firstName} ${expense.user.lastName}</td>
                <td>${expense.user.email}</td>
                <td>${expense.paymentMode}</td>
                <td>${expense.amount}</td>
                <td>${expense.description}</td>
                <td>${expense.createdAt}</td>
                <td><button type='button' class='btn btn-primary' onclick="operations('edit',${expense.id},this)">Edit</button>
                <button type='button' class='btn btn-danger' onclick="operations('delete',${expense.id},this)">Delete</button></td>
              </tr>
            `;
            table.append(row);
          });
        }
//        else{
//          $('#msg').text(response.message);
//          table.hide();
//        }
      })
      .catch(error => {
        alert(error);
        console.log('error', error)
      });
}

function loadUsername() {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };
    const nameSelect = $('#fullname-dropdown');
    fetch(backendServerUrl+"/user?projection=CreateUserPage", requestOptions)
      .then(result => result.json())
      .then(response => {

        if(response.status == 200 && response.data != ''){
          // Iterate over the response data and add rows to the table
          response.data.forEach(user => {
            const row = `
                <option value='${user.id}'>${user.firstName} ${user.lastName}</option>
            `;
            nameSelect.append(row);
          });
        }
      })
      .catch(error => {
        alert(error);
        console.log('error', error)
      });
}

function createExpense(){
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", window.sessionStorage.getItem("token"));

    var raw = JSON.stringify({
      "paymentMode": $("#payment-mode").val(),
      "amount": $("#amount").val(),
      "description": $("#description").val()
    });

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

    fetch(backendServerUrl+"/user/"+$("#fullname-dropdown").val()+"/expense", requestOptions)
      .then(response => response.json())
      .then(result => {
          if(result.status == 200 && result.data != ''){
            alert(result.message);
            window.location.href="expense-listing.html"
          }
          else{
            alert("Expense not added");
            window.location.reload();
          }
      })
      .catch(error => console.log('error', error));
}

function logout(){
  var localToken=window.sessionStorage.removeItem("token");
  alert("Logout Successfully...");
  window.location.reload();
}