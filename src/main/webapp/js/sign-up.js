function signUp() {
    const first_name = document.getElementById("firstName").value;
    const last_name = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;


    const user_DTO = {
        first_name: first_name,
        last_name: last_name,
        email: email,
        password: password
    }
    console.log(user_DTO);
    fetch("signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user_DTO)

    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error");
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                swal("Opps!", data.message, "success");
                window.location.href = "verify.html";
            } else {
                swal("Opps!", data.message, "error");
            }
        })
        .catch(error => {
            console.log('Error :', error);
            alert("There was an error signing up. Please try again.");
        });
}
