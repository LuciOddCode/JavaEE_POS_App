
getAllItems();

$("#btnGetAllItem").click(function () {
    getAllItems();
});

function getAllItems() {
    $("#tblItem").empty();
    $.ajax({
        url: 'http://localhost:8080/Back_End_Web_exploded/item',
        method: "get",
        dataType: "json",
        success: function (items) {
            for (let item of items) {
                let row = "<tr><td>" + item.code+ "</td><td>" + item.description + "</td><td>" + item.unitPrice  + "</td><td>" + item.qty+ "</td></tr>";
                $("#tblItem").append(row);
            }
        },
        error: function (error) {
            console.log(error.responseJSON);
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
            alert(res.message);
            getAllItems();
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
        dataType: 'json',
        success: function (resp) {
            console.log(resp);
            alert(resp.message);
            getAllItems();
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
