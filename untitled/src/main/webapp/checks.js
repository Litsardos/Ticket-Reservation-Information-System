// window.onload = function() {
//     const form = document.querySelector("form");
//     const passwordField = document.getElementById("password");
//     const confirmPasswordField = document.getElementById("confirm-password");
//     const passwordMessage = document.getElementById("passwordMessage");
//     const matchMessage = document.getElementById("matchMessage");
//     const submitBtn = document.getElementById("submitBtn");
//     const userTypeRadios = document.getElementsByName("type");
//     const lkLabel = document.querySelector("label[for='lk']");
//     const birthdateField = document.getElementById("birthdate");
//     const checkStrengthBtn = document.getElementById("checkStrengthBtn");
//     const resultDiv = document.getElementById("result");
//
//
//     let volunteerFieldsContainer = document.createElement('div');
//     volunteerFieldsContainer.id = 'volunteerFieldsContainer';
//     volunteerFieldsContainer.style.display = 'none';
//
//
//     let volunteerTypeLabel = document.createElement('label');
//     volunteerTypeLabel.htmlFor = "volunteer_type";
//     volunteerTypeLabel.textContent = "Κατηγορία:";
//     let volunteerTypeSelect = document.createElement('select');
//     volunteerTypeSelect.name = "volunteer_type";
//     volunteerTypeSelect.id = "volunteer_type";
//     let opt1 = document.createElement('option');
//     opt1.value = "simple";
//     opt1.textContent = "Μάχιμοι Εθελοντές πυροσβέστες";
//     let opt2 = document.createElement('option');
//     opt2.value = "driver";
//     opt2.textContent = "Οδηγοί-Χειριστές Πυροσβεστικών οχημάτων";
//     volunteerTypeSelect.appendChild(opt1);
//     volunteerTypeSelect.appendChild(opt2);
//
//     volunteerFieldsContainer.appendChild(volunteerTypeLabel);
//     volunteerFieldsContainer.appendChild(volunteerTypeSelect);
//     volunteerFieldsContainer.appendChild(document.createElement('br'));
//     volunteerFieldsContainer.appendChild(document.createElement('br'));
//
//
//     let heightLabel = document.createElement('label');
//     heightLabel.htmlFor = "height";
//     heightLabel.textContent = "Ύψος (1.45 - 2.20):";
//     let heightInput = document.createElement('input');
//     heightInput.type = "number";
//     heightInput.id = "height";
//     heightInput.name = "height";
//     heightInput.min = "1.45";
//     heightInput.max = "2.20";
//     heightInput.step = "0.01";
//
//     volunteerFieldsContainer.appendChild(heightLabel);
//     volunteerFieldsContainer.appendChild(heightInput);
//     volunteerFieldsContainer.appendChild(document.createElement('br'));
//     volunteerFieldsContainer.appendChild(document.createElement('br'));
//
//
//     let weightLabel = document.createElement('label');
//     weightLabel.htmlFor = "weight";
//     weightLabel.textContent = "Βάρος (40-160 kg):";
//     let weightInput = document.createElement('input');
//     weightInput.type = "number";
//     weightInput.id = "weight";
//     weightInput.name = "weight";
//     weightInput.min = "40";
//     weightInput.max = "160";
//
//     volunteerFieldsContainer.appendChild(weightLabel);
//     volunteerFieldsContainer.appendChild(weightInput);
//     volunteerFieldsContainer.appendChild(document.createElement('br'));
//     volunteerFieldsContainer.appendChild(document.createElement('br'));
//
//
//     let volunteerRadio = document.getElementById("type_volunteer");
//     volunteerRadio.parentNode.insertBefore(volunteerFieldsContainer, volunteerRadio.nextSibling.nextSibling);
//
//
//     for (let i=0; i<userTypeRadios.length; i++) {
//         userTypeRadios[i].addEventListener('change', function() {
//             if (isVolunteer()) {
//                 volunteerFieldsContainer.style.display = 'block';
//                 heightInput.required = true;
//                 weightInput.required = true;
//             } else {
//                 volunteerFieldsContainer.style.display = 'none';
//                 heightInput.required = false;
//                 weightInput.required = false;
//             }
//         });
//     }
//
//
//     passwordField.addEventListener('input', checkPasswords);
//     confirmPasswordField.addEventListener('input', checkPasswords);
//
//
//     checkStrengthBtn.addEventListener('click', function() {
//         let pwd = passwordField.value.trim();
//         let strength = checkPasswordStrength(pwd);
//         if (strength === "weak") {
//             passwordMessage.textContent = "Weak password!";
//         } else if (strength === "medium") {
//             passwordMessage.textContent = "Medium password!";
//         } else if (strength === "strong") {
//             passwordMessage.textContent = "Strong password!";
//         } else {
//             passwordMessage.textContent = "";
//         }
//     });
//
//
//     form.addEventListener('submit', function(e) {
//         console.log("Form submitted");
//         if (!validateForm()) {
//             e.preventDefault(); // Ακύρωση αποστολής αν δεν περάσει η επικύρωση
//             console.log("Form validation failed");
//         } else {
//             e.preventDefault(); // Ακύρωση της default συμπεριφοράς
//             console.log("Form validation passed");
//             sendFormDataToServer(); // Αποστολή των δεδομένων στον server
//         }
//     });
//
//
//     function checkPasswords() {
//         // Reset messages
//         matchMessage.textContent = "";
//
//         let pwd = passwordField.value.trim();
//         let cpwd = confirmPasswordField.value.trim();
//
//
//         if (pwd !== cpwd && cpwd.length > 0) {
//             matchMessage.textContent = "Τα passwords δεν ταιριάζουν!";
//         } else {
//             matchMessage.textContent = "";
//         }
//     }
//
//
//     window.togglePasswordVisibility = function(fieldId, buttonId) {
//         let field = document.getElementById(fieldId);
//         let btn = document.getElementById(buttonId);
//         if (field.type === "password") {
//             field.type = "text";
//             btn.textContent = "Show/Hide";
//         } else {
//             field.type = "password";
//             btn.textContent = "Show/Hide";
//         }
//     }
//
//     function isVolunteer(){
//         let typeSelected = document.querySelector('input[name="type"]:checked').value;
//         return (typeSelected === "volunteer");
//     }
//
//     function validateForm() {
//         let pwd = passwordField.value.trim();
//         let cpwd = confirmPasswordField.value.trim();
//
//
//         if (pwd !== cpwd) {
//             matchMessage.textContent = "Τα passwords δεν ταιριάζουν!";
//             return false;
//         }
//
//
//         let strength = checkPasswordStrength(pwd);
//         passwordMessage.textContent = "";
//         if (strength === "weak") {
//             passwordMessage.textContent = "Weak password!";
//             return false;
//         } else if (strength === "medium") {
//             passwordMessage.textContent = "Medium password!";
//         } else if (strength === "strong") {
//             passwordMessage.textContent = "Strong password!";
//         }
//
//
//         if (isVolunteer()) {
//             let birthdate = birthdateField.value;
//             if (!checkVolunteerAge(birthdate)) {
//                 alert("Ως εθελοντής πυροσβέστης πρέπει να είστε 18 έως 55 ετών.");
//                 return false;
//             }
//
//             let heightInput = document.getElementById("height");
//             let weightInput = document.getElementById("weight");
//             let volunteerTypeSelect = document.getElementById("volunteer_type");
//             if (!volunteerTypeSelect.value || !heightInput.value || !weightInput.value) {
//                 alert("Παρακαλώ συμπληρώστε τα στοιχεία του εθελοντή πυροσβέστη.");
//                 return false;
//             }
//         }
//
//
//         let lkChecked = document.getElementById('lk').checked;
//         if (!lkChecked) {
//             alert("Πρέπει να συμφωνήσετε με τους όρους χρήσης.");
//             return false;
//         }
//
//         return true;
//     }
//
//     function checkPasswordStrength(pwd) {
//         if (pwd.length === 0) return "weak";
//
//
//         let forbiddenWords = ["fire", "fotia", "ethelontis", "volunteer"];
//         let lowerPwd = pwd.toLowerCase();
//         for (let fw of forbiddenWords) {
//             if (lowerPwd.includes(fw)) {
//                 return "weak";
//             }
//         }
//
//
//         let digitsCount = (pwd.match(/\d/g) || []).length;
//         if (digitsCount >= pwd.length/2) {
//             return "weak";
//         }
//
//
//         let charCount = {};
//         for (let i=0; i<pwd.length; i++){
//             let c = pwd.charAt(i);
//             charCount[c] = (charCount[c] || 0) + 1;
//         }
//         for (let c in charCount) {
//             if (charCount[c] >= pwd.length/2) {
//                 return "weak";
//             }
//         }
//
//
//
//         let hasSymbol = /[\W_]/.test(pwd);
//         let hasUpper = /[A-Z]/.test(pwd);
//         let hasLower = /[a-z]/.test(pwd);
//         let hasDigit = /\d/.test(pwd);
//
//         if (hasSymbol && hasUpper && hasLower && hasDigit) {
//             return "strong";
//         }
//
//
//         return "medium";
//     }
//
//     function sendFormDataToServer() {
//         let formData = new FormData(form);
//
//         formData.append("lat", "10.0");
//         formData.append("lon", "20.0");
//
//
//         let obj = {};
//         for (let [key, value] of formData.entries()) {
//             if (value.trim() !== "") { // Μόνο μη κενές τιμές
//                 obj[key] = value;
//             }
//         }
//         console.log("JSON being sent to server:", JSON.stringify(obj));
//
//         fetch('/untitled_war_exploded/NewServlet', {
//
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(obj)
//         })
//             .then(response => response.text())
//             .then(data => {
//                 if (data === "success") {
//                     alert("Επιτυχής εγγραφή!");
//                 } else {
//                     alert("Σφάλμα: " + data);
//                 }
//             })
//             .catch(error => {
//                 console.error("Σφάλμα κατά την αποστολή των δεδομένων:", error);
//                 alert("Σφάλμα κατά την αποστολή των δεδομένων.");
//             });
//     }
//
//
// };