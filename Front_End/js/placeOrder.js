
//order


//load all customers into combo box
loadAllCustomers();
loadAllItems();
function loadAllCustomers() {
    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/customer?option=getAll',
        method: 'get',
        dataType: 'json',
        success: function (customers) {
            for (let i in customers) {
                let id = customers[i].id;
                let name = customers[i].name;
                $("#selectCusID").append(`<option value="${id}"</option>`);
            }
        }
    });
}

//load all items into combo box
function loadAllItems() {
    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/item',
        method: 'get',
        dataType: 'json',
        success: function (items) {
            for (let i in items) {
                let code = items[i].code;
                let description = items[i].description;
                $("#selectItemCode").append(`<option value="${code}"</option>`);

            }
        }
    });
}

//load customer details when customer id is selected
$("#selectCusID").change(function () {
    let cusID = $("#selectCusID").val();
    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/customer?option=getID?cusID=' + cusID,
        method: 'get',
        dataType: 'formdata',
        success: function (customer) {
            $("#orderCustomerID").val(customer.id);
            $("#orderCustomerName").val(customer.name);
            $("#orderCustomerAddress").val(customer.address);
        }
    });
});

//load item details when item code is selected
$("#selectItemCode").change(function () {
    let itemCode = $("#selectItemCode").val();
    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/item?code=' + itemCode,
        method: 'get',
        dataType: 'json',
        success: function (item) {
            $("#txtItemCode").val(item.code);
            $("#txtItemDescription").val(item.description);
            $("#txtItemPrice").val(item.unit_price);
            $("#txtQTYOnHand").val(item.qty);
        }
    });
});

function calculateTotal() {
    let total = 0;
    $("#orderTable > tr").each(function () {
        let price = $(this).find("td:nth-child(4)").text();
        let qty = $(this).find("td:nth-child(5)").text();
        total += parseFloat(price) * parseInt(qty);
    });
    $("#total").text(total.toFixed(2));
    $("#subtotal").text(total.toFixed(2));
}

//add item to the table
$("#btnAddToTable").click(function () {
    let itemCode = $("#txtItemCode").val();
    let itemDescription = $("#txtItemDescription").val();
    let itemPrice = $("#txtItemPrice").val();
    let itemQty = $("#txtQty").val();

    let total = itemPrice * itemQty;

    let row = `<tr><td>${itemCode}</td><td>${itemDescription}</td><td>${itemPrice}</td><td>${itemQty}</td><td>${total}</td></tr>`;
    $("#orderTable").append(row);
    calculateTotal();
});

//calculate balance
$("#txtCash").keyup(function () {
    let cash = parseFloat($("#txtCash").val());
    let total = parseFloat($("#total").text());
    let balance = cash - total;
    $("#txtBalance").val(balance.toFixed(2));
});

//submit order
$("#btnSubmitOrder").click(function () {
    let orderID = $("#txtOrderID").val();
    let orderDate = $("#txtDate").val();
    let customerID = $("#orderCustomerID").val();
    let orderTotal = $("#total").text();
    let orderDiscount = $("#txtDiscount").val();
    let orderNetTotal = $("#subtotal").text();
    let orderPaid = $("#txtCash").val();
    let orderBalance = $("#txtBalance").val();

    let orderDetails = [];
    $("#orderTable > tr").each(function () {
        let itemCode = $(this).find("td:nth-child(1)").text();
        let itemQty = $(this).find("td:nth-child(4)").text();
        let itemPrice = $(this).find("td:nth-child(3)").text();
        let orderDetail = {
            orderID: orderID,
            itemCode: itemCode,
            qty: itemQty,
            unitPrice: itemPrice
        };
        orderDetails.push(orderDetail);
    });

    let order = {
        orderID: orderID,
        orderDate: orderDate,
        customerID: customerID,
        orderTotal: orderTotal,
        orderDiscount: orderDiscount,
        orderNetTotal: orderNetTotal,
        orderPaid: orderPaid,
        orderBalance: orderBalance,
        orderDetails: orderDetails
    };

    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/order',
        method: 'post',
        data: JSON.stringify(order),
        contentType: 'application/json',
        success: function (resp) {
            alert(resp.message);
        },
        error: function (error) {
            console.log(error);
        }
    });
});

//clear all
$("#btn-clear").click(function () {
    $("#txtItemCode").val("");
    $("#txtItemDescription").val("");
    $("#txtItemPrice").val("");
    $("#txtQTYOnHand").val("");
    $("#txtQty").val("");
});

$("#btn-clear1").click(function () {
    $("#txtCustomerID").val("");
    $("#txtCustomerName").val("");
    $("#txtCustomerAddress").val("");
    $("#txtCustomerSalary").val("");
});



