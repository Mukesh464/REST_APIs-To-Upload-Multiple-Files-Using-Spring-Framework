package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> imageList = new ArrayList<String>();
	
	public ImageList(){
		
	}
	
	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

}
