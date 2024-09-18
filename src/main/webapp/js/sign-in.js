async function signIn() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const user_DTO = {
        email: email,
        password: password
    }
    const response = await fetch('signIn', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user_DTO)
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    if (data.success) {
        await swal("Welcome!", data.message, "success");
        window.location.href = "index.html";
    } else if (data.message === "User not verified") {
        await swal("Opps!", data.message, "info");
        window.location.href = "verify.html";
    } else {
        swal("Opps!", data.message, "Error");
    }
}