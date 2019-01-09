package com.niit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.niit.dao.Frienddao;
import com.niit.dao.Userdao;
import com.niit.model.Errorclazz;
import com.niit.model.Friend;
import com.niit.model.User;


@Controller
public class Friendcontroller {
	@Autowired
private Frienddao friendDao;
	@Autowired
private Userdao userDao;
	@RequestMapping(value="/suggestedusers",method=RequestMethod.GET)
	public ResponseEntity<?> getAllSuggestedUsers(HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		List<User> suggestedUsers=friendDao.getAllSuggestedUsers(email);
		
		return new ResponseEntity<List<User>>(suggestedUsers,HttpStatus.OK);
	}
	@RequestMapping(value="/friendrequest",method=RequestMethod.POST)
	public ResponseEntity<?> friendRequest(@RequestBody User toId,HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		Friend friend=new Friend();
		friend.setToId(toId);
		friend.setStatus('P');
		//logged in user email id is 'email'
		friend.setFromId(userDao.getUser(email));
		
		friendDao.friendRequest(friend);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@RequestMapping(value="/pendingrequests",method=RequestMethod.GET)
	public ResponseEntity<?> pendingRequests(HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		List<Friend> pendingRequests=friendDao.pendingRequests(email);
		return new ResponseEntity<List<Friend>>(pendingRequests,HttpStatus.OK);
	}
	@RequestMapping(value="/acceptfriendrequest",method=RequestMethod.PUT)
	public ResponseEntity<?> acceptFriendRequest(@RequestBody Friend friend,HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		friendDao.acceptFriendRequest(friend);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@RequestMapping(value="/deletefriendrequest",method=RequestMethod.PUT)
	public ResponseEntity<?> deleteFriendRequest(@RequestBody Friend friend,HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		friendDao.deleteFriendRequest(friend);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@RequestMapping(value="/listoffriends",method=RequestMethod.GET)
	public ResponseEntity<?> listOfFriends(HttpSession session){
		String email=(String)session.getAttribute("email");
		//NOT LOGGED IN
		if(email==null){
			Errorclazz errorClazz=new Errorclazz(6,"Please login...");
    		return new ResponseEntity<Errorclazz>(errorClazz,HttpStatus.UNAUTHORIZED);//login.html
		}
		List<User> friends=friendDao.listOfFriends(email);
		return new ResponseEntity<List<User>>(friends,HttpStatus.OK);
	}
	@RequestMapping(value="/mutualfriends",method=RequestMethod.GET)
	public ResponseEntity<?> getMutualFriends(@RequestParam String useremail,HttpSession session){
		String email=(String)session.getAttribute("email");
		System.out.println("requestparam is " + useremail );
		 List<User> userList1=friendDao.listOfFriends(email);
		 List<User> userList2=friendDao.listOfFriends(useremail);
		 System.out.println(userList1);
		 System.out.println(userList2);
		 userList1.retainAll(userList2);
		 System.out.println(userList1);
		 return new ResponseEntity<List<User>>(userList1,HttpStatus.OK);
	}

	
}


