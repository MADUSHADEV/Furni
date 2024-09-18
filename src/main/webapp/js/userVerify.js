async function verifyUser() {
    const verificationCode = document.getElementById('verificationCode').value;
    const verifyDto = {
        verificationCode: verificationCode,
    }
    try {
        const response = await fetch('userVerify', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(verifyDto)
        });
        // // Check if response is ok
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error: ${errorText}`);
        }

        const data = await response.json(); // Parse the response
        if (!data.success) {
            swal("Opps!", data.message, 'error');
            return;
        }else {
            swal("Good job!", data.message, "success");
            window.location.href = "login.html";
        }
    } catch (error) {
        console.error('There was an error!', error.message);
    }
}