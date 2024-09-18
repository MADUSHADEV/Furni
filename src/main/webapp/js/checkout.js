async function countryLoad() {
    const response = await fetch('country', {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    console.log(data);
    const select = document.getElementById('c_country');
    data.forEach(country => {
        const option = document.createElement('option');
        option.value = country.id;
        option.textContent = country.name;
        select.appendChild(option);
    });
}

async function loadCartData() {
    const response = await fetch("cart", {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    displayCartDetails(data);
}

function displayCartDetails(cart) {
    const cartTableBody = document.querySelector('.site-block-order-table tbody');
    cartTableBody.innerHTML = ''; // Clear existing rows
    let subtotal = 0;
    cart.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.product.title} <strong class="mx-2">x</strong> ${item.qty}</td>
            <td>Rs. ${item.product.price}</td>
        `;

        subtotal += item.product.price * item.qty;
        cartTableBody.appendChild(row);
    });
    const total = subtotal;

    const subtotalRow = document.createElement('tr');
    subtotalRow.innerHTML = `
        <td class="text-black font-weight-bold"><strong>Cart Subtotal</strong></td>
        <td class="text-black">Rs. ${subtotal}</td>
    `;
    cartTableBody.appendChild(subtotalRow);

    const totalRow = document.createElement('tr');
    totalRow.innerHTML = `
        <td class="text-black font-weight-bold"><strong>Order Total</strong></td>
        <td class="text-black font-weight-bold"><strong>Rs. ${total}</strong></td>
    `;
    cartTableBody.appendChild(totalRow);
}

function mapDataToForm(data) {
    const firstName = document.getElementById('c_fname');
    const lastName = document.getElementById('c_lname');
    const addressLine1 = document.getElementById('c_address');
    const addressLine2 = document.getElementById('c_address2');
    const state = document.getElementById('c_state_country');
    const email = document.getElementById('c_email_address');
    const phone = document.getElementById('c_phone');
    const countrySelect = document.getElementById('c_country');

    if (data.first_name) firstName.value = data.first_name;
    if (data.last_name) lastName.value = data.last_name;
    if (data.email) email.value = data.email;

    if (data.addresses && data.addresses.length > 0) {
        const address = data.addresses[0];
        if (address.line1) addressLine1.value = address.line1;
        if (address.line2) addressLine2.value = address.line2;
        if (address.state) state.value = address.state;
        if (address.mobile) phone.value = address.mobile;
        if (address.country) {
            const countryOption = Array.from(countrySelect.options).find(option => option.value === address.country.toString());
            if (countryOption) countrySelect.value = countryOption.value;
        }
    }
}

async function loadAddressData() {
    const response = await fetch('address', {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    mapDataToForm(data);
    console.log(data);
}

async function saveShippingData() {
    // Get form input values
    const addressLine1 = document.getElementById('c_address').value.trim();
    const addressLine2 = document.getElementById('c_address2').value.trim();
    const state = document.getElementById('c_state_country').value.trim();
    const phone = document.getElementById('c_phone').value.trim();
    const countryId = document.getElementById('c_country').value.trim();

    // Regular expression to validate phone number (basic validation)
    const phoneRegex = /^[0-9]{10}$/;

    // Check if required fields are filled
    if (!countryId) {
        await swal("oops!", 'Country is required.', "error");
        return false;
    }

    if (!addressLine1) {
        await swal("oops!", 'Address Line 1 is required.', "error");
        return false;
    }

    if (!state) {
        await swal("oops!", 'State/City is required.', "error");
        return false;
    }

    if (!phone) {
        await swal("oops!", 'Phone number is required.', "error");
        return false;
    }

    if (!phoneRegex.test(phone)) {
        await swal("oops!", 'Phone number must be a valid 10-digit number.', "error");
        return false;
    }

    const data = {
        line1: addressLine1,
        line2: addressLine2,
        state: state,
        mobile: phone,
        country: {id: countryId}
    };
    const response = await fetch('address', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error("Error saving shipping data");
    }

    const responseData = await response.json();
    if (!responseData.success) {
        swal("oops!", responseData.message, "error");
        return;
    }
    swal("Good job!", responseData.message, "success");
}

function getInputDataAndValidate() {
    // Get form input values
    const addressLine1 = document.getElementById('c_address').value.trim();
    const addressLine2 = document.getElementById('c_address2').value.trim();
    const state = document.getElementById('c_state_country').value.trim();
    const phone = document.getElementById('c_phone').value.trim();
    const countryId = document.getElementById('c_country').value.trim();

    // Regular expression to validate phone number (basic validation)
    const phoneRegex = /^[0-9]{10}$/;

    // Check if required fields are filled
    if (!countryId) {
        swal("oops!", 'Country is required.', "error");
        return false;
    }

    if (!addressLine1) {
        swal("oops!", 'Address Line 1 is required.', "error");
        return false;
    }

    if (!state) {
        swal("oops!", 'State/City is required.', "error");
        return false;
    }

    if (!phone) {
        swal("oops!", 'Phone number is required.', "error");
        return false;
    }

    if (!phoneRegex.test(phone)) {
        swal("oops!", 'Phone number must be a valid 10-digit number.', "error");
        return false;
    }

    return {
        line1: addressLine1,
        line2: addressLine2,
        state: state,
        mobile: phone,
        country: {id: countryId}
    };
}

async function startPaymentProcess() {

    const addressData = getInputDataAndValidate();
    if (!addressData) {
        swal("oops!", 'Check your shipping detail', "error");
        return;
    }

    const response = await fetch('order', {
        method: 'POST',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    if (!data.success) {
        console.log("Error creating order");
        return;
    }

    console.log(data.message.hash);
    // Put the payment variables here
    var payment = {
        "sandbox": true,
        "merchant_id": "1224343",    // Replace your Merchant ID
        "return_url": "http://localhost:8080/furni-1.0-SNAPSHOT/checkout.html",     // Important
        "cancel_url": "http://localhost:8080/furni-1.0-SNAPSHOT/checkout.html",     // Important
        "notify_url": "http://localhost:8080/furni-1.0-SNAPSHOT/checkout.html",
        "order_id": data.message.order_id,
        "items": "",
        "amount": data.message.total_amount,
        "currency": "LKR",
        "hash": data.message.hash, // *Replace with generated hash retrieved from backend
        "first_name": "",
        "last_name": "",
        "email": "",
        "phone": "",
        "address": "",
        "city": "",
        "country": "",
        "delivery_address": "",
        "delivery_city": "",
        "delivery_country": "",
        "custom_1": "",
        "custom_2": ""
    };

    payhere.startPayment(payment);

    /*********************************************************************************/
// Payment completed. It can be a successful failure.
    payhere.onCompleted = function onCompleted(orderId) {
        console.log("Payment completed. OrderID:" + orderId);
        // Note: validate the payment and show success or failure page to the customer
        window.location.href = "thankyou.html";
    };

// Payment window closed
    payhere.onDismissed = function onDismissed() {
        // Note: Prompt user to pay again or show an error page
        console.log("Payment dismissed");
    };

// Error occurred
    payhere.onError = function onError(error) {
        // Note: show an error page
        console.log("Error:" + error);
    };


}




