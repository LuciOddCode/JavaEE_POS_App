
//customer
getAllCustomers();

$("#btnGetAll").click(function () {
    getAllCustomers();
});

function getAllCustomers() {
    $("#tblCustomer").empty();
    <!--send ajax request to the customer servlet using jQuery-->
    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/customer?option=getAll',
        method: "get",
        dataType: "json",
        success: function (customers) {
            for (let customer of customers) {
                let row = "<tr><td>" + customer.id + "</td><td>" + customer.name + "</td><td>" + customer.address + "</td></tr>";
                $("#tblCustomer").append(row);
            }
        },
        error: function (error) {
            console.log(error.responseJSON);
        }
    });
}

$("#btnCustomer").click(function () {

    let formdata = $("#customerForm").serialize();
    $.ajax({
        url: "http://localhost:8080/Back_End_Web_exploded/customer",
        method: "post",
        data: formdata,
        success: function (res) {
            alert(res.message);
            getAllCustomers();
        },
        error: function (error) {
            console.log(error.responseJSON);
            alert(error.responseJSON.message);
        }
    });


});

$("#btnCusDelete").click(function () {
    let formData = $("#txtCustomerID").val();
    $.ajax({
        url: "http://localhost:8080/Back_End_Web_exploded/customer?cusID=" + formData,
        method: 'DELETE',
        dataType: 'json',
        success: function (resp) {
            console.log(resp);
            alert(resp.message);
            getAllCustomers();
        },
        error: function (error) {
            console.log(error);
            alert(error.message);
        }
    });
});

 $("#btnUpdate").click(function () {
    /*   let customer = "{\"cusID\":\"" + $("#txtCustomerID").val() + "\",\"cusName\":\"" + $("#txtCustomerName").val()
           + "\",\"cusAddress\":\"" + $("#txtCustomerAddress").val() + "\"} ";*/
    let cusId = $("#txtCustomerID").val();
    let cusName = $("#txtCustomerName").val();
    let cusAddress = $("#txtCustomerAddress").val();

    let customer = {
        cusID: cusId ,
        cusName: cusName,
        cusAddress: cusAddress
    }


    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/customer',
        method: 'put',
        data: JSON.stringify(customer),
        contentType: 'json',
        success: function (resp) {
            console.log(resp);
            alert(resp.message);
            getAllCustomers();
        },
        error: function (error) {
            console.log(error.responseJSON);
            alert(error.responseJSON.message);
        }
    });
});
