function transliterate(word) {
    const ruToEn = {
        а: "a", б: "b", в: "v", г: "g", д: "d", е: "e", ё: "e", ж: "zh", з: "z",
        и: "i", й: "y", к: "k", л: "l", м: "m", н: "n", о: "o", п: "p", р: "r",
        с: "s", т: "t", у: "u", ф: "f", х: "kh", ц: "ts", ч: "ch", ш: "sh",
        щ: "shch", ы: "y", э: "e", ю: "yu", я: "ya"
    };
    return word
        .toLowerCase()
        .split("")
        .map(char => ruToEn[char] || char) // Транслитерация символов
        .join("");
}


function generateLogin(firstName,lastName){
    if(!firstName || !lastName){
        console.log("Invalid input");
        return;
    }

    let nameEn = transliterate(firstName);
    let lastNameEn = transliterate(lastName);

    let firstPart = nameEn.slice(0,2).toLowerCase();
    let lastPart = lastNameEn.slice(0,3).toLowerCase();
    console.log(firstPart + lastPart)
    return firstPart + lastPart;
}

document.getElementById("login").addEventListener("focus",function(){
    let firstName = document.getElementById("firstName").value.trim();
    let lastName = document.getElementById("lastName").value.trim();

    let login = generateLogin(firstName,lastName);
    document.getElementById("login").value = login;
});

let firstName = document.getElementById("firstName");
let lastName = document.getElementById("lastName");
let login = document.getElementById("login");

function checkFields (){
    if((firstName.value.trim() && lastName.value.trim()) && (firstName.value.trim().length > 2 && lastName.value.trim().length > 3)){
        login.disabled =  false;
        login.placeholder = "Введите логин";
    } else {
        login.disabled = true;
    }
}

[firstName, lastName].forEach(fieldElement =>{
    fieldElement.addEventListener("input", checkFields);
});