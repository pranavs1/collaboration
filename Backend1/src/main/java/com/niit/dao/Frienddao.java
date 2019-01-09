package com.niit.dao;

import java.util.List;

import com.niit.model.Friend;
import com.niit.model.User;

public interface Frienddao {
List<User> getAllSuggestedUsers(String email);

void friendRequest(Friend friend);
List<Friend> pendingRequests(String email);//email is email id of logged in user
void acceptFriendRequest(Friend friendRequest);

void deleteFriendRequest(Friend friendRequest);

List<User>  listOfFriends(String email);

}