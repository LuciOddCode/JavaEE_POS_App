
getAllItems();

$("#btnGetAllItem").click(function () {
    getAllItems();
});

function getAllItems() {
    $("#tblItem").empty();
    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/item',
        success: function (items) {
            let item = JSON.parse(items);
            for (let i=0;i<item[i].length;i++) {
                let code = item[i].itemCode;
                let description = item[i].description;
                let unit_price = item[i].unitPrice;
                let qty = item.qty;
                let row = `<tr><td>${code}</td><td>${description}</td><td>${unit_price}</td><td>${qty}</td></tr>`;
                $("#tblItem").append(row);
            }
        },
        error: function (error) {
            console.log(error);
        }
    });
}



$("#btnItem").click(function () {

    console.log("Click");
    let formdata = $("#itemForm").serialize();
    $.ajax({
        url: "http://localhost:8080/Back_End_Web_exploded/item",
        method: "post",
        data: formdata,
        success: function (res) {
            console.log(res);
            alert(res.message);
            getAllCustomers();
        },
        error: function (error) {
            console.log(error.responseJSON);
            alert(error.responseJSON.message);
        }
    });

});

$("#btnItemDelete").click(function () {
    let formData = $("#txtItemCode").val();
    $.ajax({
        url: "http://localhost:8080/Back_End_Web_exploded/item?code=" + formData,
        method: 'DELETE',
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

let click = $("#btnItemUpdate").click(function () {

    let code = $("itemCode").val();
    let description = $("#itemName").val();
    let unitPrice = $("#itemPrice").val();
    let qty = $("#itemQty").val();

    let item = {
        itemCode: code ,
        description: description,
        unitPrice: unitPrice,
        qty: qty,
    }


    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/item',
        method: 'put',
        data: JSON.stringify(item),
        contentType: 'application/json',
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
