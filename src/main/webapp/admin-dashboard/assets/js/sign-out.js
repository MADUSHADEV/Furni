async function signOut() {
    const response = await fetch('sign-out', {
        method: 'GET',
    });
    const data = await response.json();
    console.log(data);
    if (!data.success) {
        swal("Opps!", data.message, "Error");
        return;
    } else {
        window.location.href = "http://localhost:8080/furni-1.0-SNAPSHOT/index.html";
    }
}

// async function userChecker() {
//     const buttonSignIn = document.getElementById('sign-in-model');
//     const buttonOut = document.getElementById('sign-out-model');
//     const response = await fetch('user-checker', {
//         method: 'GET',
//     });
//     const data = await response.json();
//     console.log(data);
//     if (!data.success) {
//         buttonSignIn.style.display = 'block';
//         buttonOut.style.display = 'none';
//     } else {
//         buttonSignIn.style.display = 'none';
//         buttonOut.style.display = 'block';
//     }
// }