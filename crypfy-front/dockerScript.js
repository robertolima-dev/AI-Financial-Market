var fs = require('fs');

//create dockerfile
var dockerfilecontent = "FROM node:carbon\n" +
    "\n" +
    "# Create app directory\n" +
    "WORKDIR /usr/src/app\n" +
    "\n" +
    "#enviroment\n" +
    "ENV NODE_ENV production\n" +
    "\n" +
    "# copy node project\n" +
    "COPY ./ ./\n" +
    "\n" +
    "RUN npm install --dev\n" +
    "\n" +
    "RUN npm run build\n" +
    "\n" +
    "EXPOSE 9000\n" +
    "CMD [\"npm\", \"start\"]"

//write dockerfile
fs.writeFile("./Dockerfile",dockerfilecontent,function(err) {
    if(err)
        return console.log(err);
    else {
        var exec = require('child_process').exec;
        var version = process.env.npm_package_version;
        var dockerRepo = '778467513455.dkr.ecr.sa-east-1.amazonaws.com/crypfy-front:'+version;
        console.log(dockerRepo);
        var awsLoginScript = 'aws ecr get-login --no-include --region sa-east-1'

        console.log('building image');
        //build docker image
        exec('docker build --no-cache=true -t ' + dockerRepo + " .",function(err,stdout,stderr) {
            console.log(err);
            console.log(stdout);
            console.log('logging on aws');
            //get fresh login token
            exec(awsLoginScript,function(err,stdout,stderr) {
                console.log(err);
                console.log(stdout);
                //login aws ecr docker repo
                exec(stdout,function(err,stdout,stderr) {
                    console.log(err);
                    console.log(stdout);
                    //push docker image
                    exec("docker push " + dockerRepo,function(err,stdout,stderr) {
                        console.log(err);
                        console.log(stdout);
                    })
                })
            })
        })
    }
})