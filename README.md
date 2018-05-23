# REST_APIs-To-Upload-Multiple-Files-Spring-Framework
REST_APIs to upload Multiple files and download a file using Spring-Framework
build the project with command mvn clean install
deploy the war file in tomcat 
To upload a file use @RequestMapping(value = "/uploadimages", method = RequestMethod.POST) with name myfiles.
Files will be stored under C:\images at server side.
To get the list of the uploaded files use @RequestMapping(value = "/imagelist", method = RequestMethod.GET). It will return the urls of all uploaded images.
To download the file click the url of the file or paste the url in the browser.
