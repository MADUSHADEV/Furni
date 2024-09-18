async function loadInterior() {
    const response = await fetch('admin-product', {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    if (!data.success) {
        alert(data.message);
    }
    const category = data.message.category;
    console.log(category);
    loadSelection('exampleFormControlSelect1 select-interior', category, ['id', 'name']);

}

function loadSelection(selectedId, list, propertyArray) {
    const selectTag = document.getElementById(selectedId);
    list.forEach((item) => {
        let option = document.createElement('option');
        option.value = item[propertyArray[0]];
        option.text = item[propertyArray[1]];
        selectTag.appendChild(option);
    });
}

async function addProduct() {
    const categoryId = document.getElementById('exampleFormControlSelect1 select-interior').value;
    const productTitle = document.getElementById('squareInput product-name').value;
    const productDescription = document.getElementById('exampleFormControlTextarea1 product-description').value;
    const price = document.getElementById('product-price').value;
    const quantity = document.getElementById('squareInput product-qty').value;
    const image1 = document.getElementById('squareInput product-image');

    console.log(categoryId, productTitle, productDescription, price, quantity, image1);

    let formData = new FormData();
    formData.append('categoryId', categoryId);
    formData.append('productTitle', productTitle);
    formData.append('productDescription', productDescription);
    formData.append('price', price);
    formData.append('quantity', quantity);
    formData.append('image1', image1.files[0]);

    const response = await fetch('admin-product',
        {
            method: 'POST',
            body: formData
        });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    if (!data.success) {
        swal("Opps!", data.message, "Error");
        return;
    }
    await swal("Good job!", data.message, "success");
    window.location.reload();
}

function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function () {
        const output = document.getElementById('image-preview');
        output.src = reader.result;
        output.style.display = 'block';
    };
    reader.readAsDataURL(event.target.files[0]);
}