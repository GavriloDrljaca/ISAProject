var translation = angular.module('translation', ['pascalprecht.translate']);

translation.config(function ($translateProvider) {
    $translateProvider.translations('en', {
        
        GENDER: 'Gender',
        MALE: 'Male',
        FEMALE: 'Female',
        DATE_OF_BIRTH: 'Date of birth',
       
        ERROR: 'ERROR',
        ERROR_ACCOUNT_NOT_FOUND: 'Please contact the administrator to enable your account.',
        ERROR_REQUIRED_FIELD: 'This field is required',
        PASSWORD_REPEAT : 'Ponovite lozinku',
        PASSWORD : 'Password',
        USERNAME: 'Username',
        LOGIN: 'Login',
        REGISTER: 'Register',
        EMAIL_ADDRESS: 'Email address',
        CANCEL: 'Cancel',
        SAVE: 'Save',
        USERNAME_EXISTS: 'Username already exists. Choose another',
        PASSWORD_NOT_EQUAL: 'Password not equal',
        PASSWORD_NOT_EQUAL_MESSAGE: 'Please enter correct password',
        OK: 'Ok'
    });
    $translateProvider.translations('srb', {
        
        GENDER: 'Pol',
        MALE: 'Muški',
        FEMALE: 'Ženski',
        DATE_OF_BIRTH: 'Datum rođenja',
        NAME : 'Ime',
        USERNAME: 'Korisničko ime',
        PASSWORD: 'Lozinka',
        ERROR: 'GREŠKA',
        ERROR_REQUIRED_FIELD: 'Ovo polje je obavezno!',
        PASSWORD_REPEAT : 'Ponovite lozinku',
        LOGIN: 'Prijavi se',
        REGISTER: 'Registruj se',
        EMAIL_ADDRESS: 'Email adresa',
        CANCEL: 'Odustani',
        SAVE: 'Sačuvaj',
        USERNAME_EXISTS: 'Korisnicko ime vec postoji. Izaberite drugo.',
        PASSWORD_NOT_EQUAL: 'Ponovljena lozinka nije ispravna.',
        PASSWORD_NOT_EQUAL_MESSAGE: 'Ponovljena loznika nije ispravna. Molimo vas unesite ispravnu.',
        OK: "U redu"
        
    });

    $translateProvider.preferredLanguage('srb');
});