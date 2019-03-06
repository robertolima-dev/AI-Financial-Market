import StringMask from 'string-mask'

const stringUtils = {
    toMoneyFormat: (number,c, d, t) => {
        var n = number,
            c = isNaN(c = Math.abs(c)) ? 2 : c,
            d = d == undefined ? "." : d,
            t = t == undefined ? "," : t,
            s = n < 0 ? "-" : "",
            i = String(parseInt(n = Math.abs(Number(n) || 0).toFixed(c))),
            j = (j = i.length) > 3 ? j % 3 : 0;
        return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
    },
    toDecimal: (val) => {
        return parseFloat(val.replaceAll('.','').replace(',','.'));
    },
    toBitcoin: (val) => {
        return val.toFixed(8);
    },
    isUpperCase(str) {
        return str == str.toUpperCase();
    },
    removeAccent(str) {
        let accents = 'ÀÁÂÃÄÅàáâãäåßÒÓÔÕÕÖØòóôõöøÈÉÊËèéêëðÇçÐÌÍÎÏìíîïÙÚÛÜùúûüÑñŠšŸÿýŽž';
        let accentsOut = "AAAAAAaaaaaaBOOOOOOOooooooEEEEeeeeeCcDIIIIiiiiUUUUuuuuNnSsYyyZz";
        str = str.split('');
        str.forEach((letter, index) => {
            let i = accents.indexOf(letter);

            if (i != -1)
                str[index] = accentsOut[i];
        });
        return str.join('');
    },
    formatSatBarCode(barcode) {
        let split = barcode.match(/.{1,4}/g);
        let str = split[0];
        for (var i = 1; i < split.length; i++) {
            str = str + ' ' + split[i];
        }
        return str;
    },
    toSatFormPayment(formPayment) {
        switch (formPayment) {
            case 'CASH':
                return '01';
            case 'CHECK':
                return '02';
            case 'CREDIT':
                return '03';
            case 'DEBIT':
                return '04';
            default:
                return '';
        }
    },
    getExtensionFileFromUrl(url) {
        let splited = url.split('/');
        let splited2 = splited[splited.length-1].split('.');
        return splited2[1];
    },
    pad(num,size) {
        var s = num+"";
        while (s.length < size) s = "0" + s;
        return s;
    },
    validateCpf(inputCPF) {
        let promise = new Promise((resolve, reject) => {
            let sum = 0;
            let trash;

            if(inputCPF == '') resolve(true);

            if(inputCPF == '00000000000' || inputCPF == '11111111111' || inputCPF == '22222222222' || inputCPF == '33333333333' || inputCPF == '44444444444'
                || inputCPF == '55555555555' || inputCPF == '66666666666' || inputCPF == '77777777777' || inputCPF == '88888888888'
                || inputCPF == '99999999999') resolve(false);

            for(let i=1; i<=9; i++) sum = sum + parseInt(inputCPF.substring(i-1, i)) * (11 - i);
            trash = (sum * 10) % 11;

            if((trash == 10) || (trash == 11)) trash = 0;
            if(trash != parseInt(inputCPF.substring(9, 10))) resolve(false);

            sum = 0;
            for(let i = 1; i <= 10; i++) sum = sum + parseInt(inputCPF.substring(i-1, i))*(12-i);
            trash = (sum * 10) % 11;

            if((trash == 10) || (trash == 11)) trash = 0;
            if(trash != parseInt(inputCPF.substring(10, 11))) resolve(false);
            resolve(true);
        });
        return promise;
    },
    cpfPattern : "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}",
    toCpf: (cpf) => {
        var formatter = new StringMask('000.000.000-00');
        return formatter.apply(cpf);
    },
    toCnpj: (cpf) => {
        var formatter = new StringMask('00.000.000/0000-00');
        return formatter.apply(cpf);
    },
    emailPattern: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    getHumanNameDepositWithdrawStatus(id) {
        if(id == 'WAITING_PHOTO_UPLOAD')
            return "AGUARDANDO COMPROVANTE";
        if(id == 'CANCELLED')
            return "CANCELADO";
        if(id == 'CONFIRMED')
            return "CONFIRMADO";
        if(id == "DENIED")
            return "NEGADO";
        if(id == 'WAITING_APPROVAL')
            return "AGUARDANDO APROVAÇÃO";
        if(id == 'PROCESSING')
            return "PROCESSANDO";
        return "";
    },
    getHumanNamePlanStatus(id) {
        if(id == 'IN_PROGRESS')
            return "Vigente";
        if(id == 'FINISHED')
            return "Finalizado";
        if(id == 'PROCESSING')
            return "Processando";
        return "";
    },
    getHumanNameBankAccountType(id) {
        if(id == 'CONTA_CORRENTE')
            return "CONTA CORRENTE";
        if(id == "CONTA_POUPANCA")
            return "CONTA POUPANÇA";
        return "";
    }
}

export default stringUtils;
