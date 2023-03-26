const jwt1 = localStorage.getItem('jwt');
if (!jwt1) {
  window.location.href = '/hello'; // Redirect to login page
  localStorage.setItem('jwt1','token')
} 

const jwt = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';

const tokenParts = jwt.split('.'); // Split the token into header, payload, and signature parts
const tokenPayload = tokenParts[1];

const decodedPayload = atob(tokenPayload); // Decode the base64-encoded payload
const payloadObj = JSON.parse(decodedPayload); // Parse the JSON data

console.log(payloadObj); // { sub: "1234567890", name: "John Doe", iat: 1516239022 }
console.log(payloadObj.sub); // "1234567890"
console.log(payloadObj.name); // "John Doe"
console.log(payloadObj.iat); // 1516239022