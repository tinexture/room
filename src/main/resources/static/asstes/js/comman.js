function validateJwtToken(accessToken) {
    return true;
    // const token = 'your_jwt_token_here';
    // const [header, payload, signature] = token.split('.');
    // const decodedPayload = atob(payload);
    // const decodedPayloadObj = JSON.parse(decodedPayload);
    // const expTime = decodedPayloadObj.exp * 1000; // convert to milliseconds
    // const currentTime = Date.now();
    // if (expTime < currentTime) {
    //     // Token expired
    // }
    // const secretKey = 'your_secret_key_here';
    // const data = `${header}.${payload}`;
    // const encoder = new TextEncoder();
    // const keyData = encoder.encode(secretKey);
    // const signatureData = encoder.encode(signature);
    // const algorithm = { name: 'HMAC', hash: { name: 'SHA-256' } };
    // const key =  crypto.subtle.importKey('raw', keyData, algorithm, false, ['sign']);
    // const expectedSignature =  crypto.subtle.sign(algorithm, key, encoder.encode(data));
    // if (btoa(expectedSignature) !== signature) {
    //     // Invalid signature
    // }
    // Token is valid

    // const jwt = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';

    // const tokenArray = jwt.split('.');
    // const header = tokenArray[0];
    // const payload = tokenArray[1];
    // const signature = tokenArray[2];

    // const decodedHeader = atob(header);
    // const decodedPayload = atob(payload);

    // const headerObject = JSON.parse(decodedHeader);
    // const payloadObject = JSON.parse(decodedPayload);

    // const secret = 'mysecretkey';
    // const signatureToVerify = `${header}.${payload}`;
    // const expectedSignature = btoa(crypto.createHmac('sha256', secret).update(signatureToVerify).digest());

    // if (signature === expectedSignature) {
    //     console.log('JWT token is valid!');
    // } else {
    //     console.log('JWT token is invalid!');
    // }

    // const jwtParts = token.split('.');
    // console.log(jwtParts);
    // if (jwtParts.length !== 3) {
    //     return false;
    // }
    // // const jwtHeader = JSON.parse(atob(jwtParts[0]));
    // const jwtPayload = JSON.parse(atob(jwtParts[1]));
    // const jwtSignature = jwtParts[2];
    // const signedData = jwtParts[0] + '.' + jwtParts[1];
    // const secretKey = 'room-management-system'; // Replace with your own secret key
    // // console.log(jwtHeader);
    // console.log(jwtPayload);

    // // Verify the signature
    // const crypto = window.crypto || window.msCrypto; // Use window.crypto for modern browsers
    // const encoder = new TextEncoder();
    // const hmacKey = crypto.subtle.importKey('raw', encoder.encode(secretKey), 'HS256', false, ['sign']);
    // const signature = crypto.subtle.sign('HS256', hmacKey, encoder.encode(signedData));
    // const expectedSignature = btoa(String.fromCharCode.apply(null, new Uint8Array(signature)));
    // console.log(jwtSignature !== expectedSignature);
    // if (jwtSignature !== expectedSignature) {
    //     return false;
    // }
    // console.log();
    // // Verify the expiration time
    // const currentTime = Math.floor(Date.now() / 1000);
    // if (jwtPayload.exp < currentTime) {
    //     return false;
    // }

    // // Verify the issuer
    // if (jwtPayload.iss !== 'myissuer') { // Replace with your own issuer
    //     return false;
    // }

    // // Verify the audience
    // if (jwtPayload.aud !== 'myaudience') { // Replace with your own audience
    //     return false;
    // }

    // return true;
}

var sessionCheck = false;
var accessToken = window.sessionStorage.getItem("token");
// console.log(accessToken);
// console.log(accessToken != null);
if (accessToken != null) {
    if (validateJwtToken(accessToken)) {
        sessionCheck = true;
    }
}
var pathArray = window.location.pathname.split("/");
var extractedPath = pathArray[pathArray.length - 1];
// console.log(extractedPath);
if (sessionCheck == false) {
    if (extractedPath != "login.html" && extractedPath != "registration.html" && extractedPath != "forgote-pass.html") {
       
        window.location.href = "login.html";
    }
    window.localStorage.removeItem("token");
    // console.log("your token not valid");
}
else {

    if (extractedPath == "login.html" || extractedPath == "registration.html") {
        window.location.href = "homePage.html";

    }
}
