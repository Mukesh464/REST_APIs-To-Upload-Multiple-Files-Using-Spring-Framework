package sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import models.ImageList;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@RestController
public class SampleController {
	
	public static final String path = "C:\\images";
	
	public static final String fileName="listfiles.txt";
	
	public static final String prefix = "localhost:8080/Sample/downloadfile/";
	
	@RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
	public void uploadImage(@RequestParam(name = "myfile") CommonsMultipartFile file) {
		String filename=file.getOriginalFilename();  
        try{  
        byte barr[]=file.getBytes();  
          
        BufferedOutputStream bout=new BufferedOutputStream(  
                 new FileOutputStream(path+File.separator+filename));  
        bout.write(barr);  
        bout.flush();  
        bout.close();  
        File listfile = new File(path+File.separator+fileName);
        if(!listfile.exists()){
        	listfile.createNewFile();
        }
        BufferedWriter bw= new BufferedWriter(new FileWriter(listfile.getAbsoluteFile(), true));
        bw.write(prefix + filename);
        bw.newLine();
        bw.close();
        }catch(Exception e){System.out.println(e);}  
	}
	
	@RequestMapping(value = "/uploadimages", method = RequestMethod.POST)
	public void uploadImages(@RequestParam(name = "myfiles") CommonsMultipartFile[] files) {
		for(CommonsMultipartFile file : files){
			String filename=file.getOriginalFilename();  
	        try{  
	        byte barr[]=file.getBytes();  
	          
	        BufferedOutputStream bout=new BufferedOutputStream(  
	                 new FileOutputStream(path+File.separator+filename));  
	        bout.write(barr);  
	        bout.flush();  
	        bout.close();  
	        File listfile = new File(path+File.separator+fileName);
	        if(!listfile.exists()){
	        	listfile.createNewFile();
	        }
	        BufferedWriter bw= new BufferedWriter(new FileWriter(listfile.getAbsoluteFile(), true));
	        bw.write(prefix + filename);
	        bw.newLine();
	        bw.close();
	        }catch(Exception e){System.out.println(e);}  
		}
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test(HttpServletResponse response) throws IOException {
		String errorMessage = "Tested Correcly!!";
        System.out.println(errorMessage);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
        outputStream.close();
	}
	
	@RequestMapping(value = "/imagelist", method = RequestMethod.GET)
	@ResponseBody
	public ImageList imageList() throws IOException {
		List<String> imageListString = new ArrayList<>();
		File listfile = new File(path+File.separator+fileName);
        if(listfile.exists()){
        	BufferedReader br= new BufferedReader(new FileReader(listfile.getAbsoluteFile()));
        	String currentfilePath;
        	while ((currentfilePath =  br.readLine()) != null) {
        		imageListString.add(currentfilePath);
        	}
	        br.close();
        }
		ImageList imageList = new ImageList();
		imageList.setImageList(imageListString);
		return imageList;
	}
	
	@RequestMapping(value = "/downloadfile/{filename:.+}", method = RequestMethod.GET)
  	  public void downloadFile(HttpServletResponse response, @PathVariable("filename") String filename) throws IOException {
	    // Retrieve the file 
	    File file = new File(path + File.separator + filename);
	    if(!file.exists()){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
         
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
         
        response.setContentType(mimeType);
         
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
 
         
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
         
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
 
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
 
}
