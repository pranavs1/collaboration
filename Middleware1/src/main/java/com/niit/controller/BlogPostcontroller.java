package com.niit.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.niit.dao.BlogPostdao;
import com.niit.dao.Notificationdao;
import com.niit.dao.Userdao;
import com.niit.model.BlogPost;
import com.niit.model.Errorclazz;
import com.niit.model.Notification;
import com.niit.model.User;


@Controller
public class BlogPostcontroller {
	@Autowired
private BlogPostdao blogPostDao;
@Autowired
private Userdao userDao;
@Autowired
private Notificationdao  notificationDao;
	// input  : {'blogTitle':'..','blogContent':'.....'}
	@RequestMapping(value="/addblogpost",method=RequestMethod.POST)
	public ResponseEntity<?> addBlogPost(@RequestBody BlogPost blogPost,HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		blogPost.setPostedOn(new Date());
		User postedBy=userDao.getUser(email);
		blogPost.setPostedBy(postedBy);
		try{
		blogPostDao.addBlogPost(blogPost);
		}catch(Exception e){
			Errorclazz errorClazz=new Errorclazz(8,"Unable to save blog post details.." + e.getMessage());
			return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
	}
	@RequestMapping(value="/blogswaitingforapproval",method=RequestMethod.GET)
	public ResponseEntity<?> getBlogsWaitingForApproval(HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		//ROLE - AUTHORIZATION
		User user=userDao.getUser(email);
		if(!user.getRole().equals("ADMIN")){
			Errorclazz errorClazz=new Errorclazz(9,"You are not authorized to view the content..");
			return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		List<BlogPost> blogsWaitingForApproval=blogPostDao.getBlogsWaitingForApproval();
		return new ResponseEntity<List<BlogPost>>(blogsWaitingForApproval,HttpStatus.OK);
	}
	@RequestMapping(value="/blogsapproved",method=RequestMethod.GET)
	public ResponseEntity<?> getBlogsApproved(HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		
		List<BlogPost> blogsApproved=blogPostDao.getBlogsApproved();
		return new ResponseEntity<List<BlogPost>>(blogsApproved,HttpStatus.OK);
	}
	@RequestMapping(value="/getblog/{blogId}",method=RequestMethod.GET)
	public ResponseEntity<?> getBlog(@PathVariable int blogId,HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		BlogPost blogPost=blogPostDao.getBlog(blogId);
		return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
	}
	@RequestMapping(value="/approveblogpost/{blogPostId}")
	public ResponseEntity<?> approveBlogPost(@PathVariable int blogPostId,HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		//ROLE - AUTHORIZATION
		User user=userDao.getUser(email);
		if(!user.getRole().equals("ADMIN")){
			Errorclazz errorClazz=new Errorclazz(9,"You are not authorized to view the content..");
			return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		blogPostDao.approveBlogPost(blogPostId);
		
		BlogPost blogPost=blogPostDao.getBlog(blogPostId);
		Notification notification=new Notification();
		notification.setBlogTitle(blogPost.getBlogTitle());
		notification.setStatus("Approved");//value of status is "Approved"  or "Rejected"
		notification.setUserToBeNotified(blogPost.getPostedBy().getEmail());
		notificationDao.addNotification(notification);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/rejectblogpost/{blogPostId}/{rejectionReason}")
	public ResponseEntity<?> rejectBlogPost(@PathVariable int blogPostId,@PathVariable String rejectionReason,HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		//ROLE - AUTHORIZATION
		User user=userDao.getUser(email);
		if(!user.getRole().equals("ADMIN")){
			Errorclazz errorClazz=new Errorclazz(9,"You are not authorized to view the content..");
			return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		BlogPost blogPost=blogPostDao.getBlog(blogPostId);
		
		Notification notification=new Notification();
		notification.setBlogTitle(blogPost.getBlogTitle());
		notification.setStatus("Rejected");
		notification.setUserToBeNotified(blogPost.getPostedBy().getEmail());
		notification.setRejectionReason(rejectionReason);
		notificationDao.addNotification(notification);
		blogPostDao.rejectBlogPost(blogPostId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@RequestMapping(value="/updateblogpost",method=RequestMethod.PUT)
	public ResponseEntity<?> updateBlogPost(@RequestBody BlogPost blogPost,HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		//SAME USER WHO POSTED THE BLOGPOST IS TRYING TO UPDATE
		//blogPost.getPostedBy().getEmail  - author of the blogpost (email of the user who posted the blog)
		//email  - logged in user
		if(!blogPost.getPostedBy().getEmail().equals(email)){
			Errorclazz errorClazz=new Errorclazz(9,"You are not authorized to update this blogpost");
			return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		blogPostDao.updateBlogPost(blogPost);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}


