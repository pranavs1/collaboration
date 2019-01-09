package com.niit.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.niit.dao.ProfilePicturedao;
import com.niit.model.Errorclazz;
import com.niit.model.ProfilePicture;


@RestController
public class ProfilePicturecontroller {
	@Autowired
private ProfilePicturedao profilePictureDao;
@RequestMapping(value="/uploadprofilepic",method=RequestMethod.POST)
public ResponseEntity<?> saveOrUpdateProfilePicture(@RequestParam MultipartFile image,HttpSession session){
	String email=(String)session.getAttribute("email");
	//NOT LOGGED IN
	if(email==null){
		Errorclazz errorClazz=new Errorclazz(6,"Please login...");
		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
	}
	ProfilePicture profilePicture=new ProfilePicture();
	profilePicture.setEmail(email);
	try {
		profilePicture.setImage(image.getBytes());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	profilePictureDao.saveOrUpdateProfilePicture(profilePicture);
	return new ResponseEntity<ProfilePicture>(profilePicture,HttpStatus.OK);
}

//<img src=http://localhost:8091/Middleware1/getimage?email='adam.e@niit.com'" alt="NO Image">
@RequestMapping(value="/getimage",method=RequestMethod.GET)
public @ResponseBody byte[] getImage(@RequestParam String email,HttpSession session){
	String authEmail=(String)session.getAttribute("email");
	//NOT LOGGED IN
	if(authEmail==null){
		return null;
	}
	ProfilePicture profilePicture=profilePictureDao.getProfilePicture(email);
	if(profilePicture==null)
		return null;
	else
		return profilePicture.getImage();
			
}

}



