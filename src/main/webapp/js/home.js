async function loadProduct() {
    const response = await fetch('shop', {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    productCardMaker(data);

}

async function addToCart(id) {
    const qty = 1;

    let formData = new FormData();
    formData.append('product_id', id);
    formData.append('qty', qty);

    const Cart_DTO = {
        product: {id: id},
        qty: qty
    }
    const response = await fetch('cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(Cart_DTO)
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    if (!data.success) {
        swal("oops!", data.message, "error");
        return;
    }
    swal("Good job!", data.message, "success");
}

async function loadCart() {
    const response = await fetch("cart", {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    console.log(data);

    const cartTableBody = document.querySelector('.site-blocks-table tbody');
    const cartItemTemplate = document.querySelector('.cart-item-template');

    let subtotal = 0;

    data.forEach((item) => {
        let cartItemClone = cartItemTemplate.cloneNode(true);
        cartItemClone.style.display = 'table-row';
        cartItemClone.querySelector('.product-thumbnail img').src = "images/product-image/" + item.product.id + ".jpg";
        cartItemClone.querySelector('.product-name h2').textContent = item.product.title;
        cartItemClone.querySelector('.product-price').textContent = "Rs." + item.product.price;
        cartItemClone.querySelector('.quantity-amount').value = item.qty;
        cartItemClone.querySelector('.product-total').textContent = "Rs." + (item.product.price * item.qty);
        cartTableBody.appendChild(cartItemClone);

        subtotal += item.product.price * item.qty;
    });

    const total = subtotal;

    document.querySelector('.cart-subtotal').textContent = "Rs." + subtotal.toFixed(2);
    document.querySelector('.cart-total').textContent = "Rs." + total.toFixed(2);

}

function setModalDetails(product) {

    console.log(product);

    const modalTitle = document.querySelector('#exampleModalLabel');
    const modalImage = document.querySelector('.modal-body .product-image');
    const modalProductTitle = document.querySelector('.modal-body .product-title');
    const modalCategory = document.querySelector('.modal-body .product-category');
    const modalProductPrice = document.querySelector('.modal-body .product-price');
    const modalProductDescription = document.querySelector('.modal-body .product-description');
    const modalQTY = document.querySelector('.modal-body .product-quentity');

    modalTitle.textContent = product.title;
    modalCategory.textContent = product.category.name;
    modalImage.src = "images/product-image/" + product.id + ".jpg";
    modalProductTitle.textContent = product.title;
    modalProductPrice.textContent = "Rs." + product.price;
    modalQTY.textContent = "QTY : " + product.qty;
    modalProductDescription.textContent = product.description;

    const myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
    document.getElementById('exampleModal').addEventListener('hidden.bs.modal', () => {
        document.querySelector('.modal-backdrop').remove();
    });
    myModal.show();
}

function updatePriceRange(value) {
    document.getElementById('minPrice').textContent = value;
    document.getElementById('maxPrice').textContent = 100000 - value;
}

async function SearchProduct() {
    const search = document.getElementById('search-product').value;
    const response = await fetch("product-search?param=" + search, {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    console.log(data.message);

    if (!data.success) {
        await swal("oops!", data.message, "error");
        window.location.reload();
    }
    productCardMaker(data.message);

}

async function loadCategory() {
    const response = await fetch('category', {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    if (!data.success) {
        await swop("Opps!", data.message, "error");
    }
    const category = data.message.category;
    loadCategorySelector('categorySelect', category, ['id', 'name']);
}

function loadCategorySelector(selectedId, list, propertyArray) {
    const selectTag = document.getElementById(selectedId);
    list.forEach((item) => {
        let option = document.createElement('option');
        option.value = item[propertyArray[0]];
        option.text = item[propertyArray[1]];
        selectTag.appendChild(option);
    });
}

async function sortProduct() {
    const categorySelectorId = document.getElementById('categorySelect').value;
    console.log(categorySelectorId);
    const minPrice = document.getElementById('minPrice').textContent;
    console.log(minPrice);
    const maxPrice = document.getElementById('maxPrice').textContent;
    console.log(maxPrice);
    const response = await fetch("sort?categoryId=" + categorySelectorId + "&maxPrice=" + maxPrice + "&minPrice=" + minPrice, {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    console.log(data.message);

    if (!data.success) {
        await swal("oops!", data.message, "error");
        window.location.reload();
    }
    productCardMaker(data.message);
}

async function productCardMaker(data) {
    const productContainer = document.getElementById('product-container');
    let productTemplate = document.querySelector('.product-template');

    // Check if productTemplate exists
    if (!productTemplate) {
        console.error("Product template not found.");
        return;
    }

    // Clear the container before adding new products
    productContainer.innerHTML = '';

    // Handle the case when data is empty
    if (!data || data.length === 0) {
        productContainer.innerHTML = '<p>No products available.</p>';
        return;
    }

    // Iterate through the products
    data.forEach((product) => {
        let productClone = productTemplate.cloneNode(true);
        productClone.style.display = 'block';
        productClone.querySelector('.product-thumbnail').href = "single-product.html?id=" + product.id;
        productClone.querySelector('.product-thumbnail').src = "images/product-image/" + product.id + ".jpg";
        productClone.querySelector('.product-title').textContent = product.title;
        productClone.querySelector('.product-price').textContent = "Rs." + product.price;

        productClone.querySelector('.icon-cross').addEventListener('click', (event) => {
            event.preventDefault();
            addToCart(product.id);
        });
        productClone.querySelector('.view-button').addEventListener('click', (event) => {
            event.preventDefault();
            setModalDetails(product);
        });

        productContainer.appendChild(productClone);
    });
}


