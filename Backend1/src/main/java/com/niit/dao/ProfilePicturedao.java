package com.niit.dao;

import com.niit.model.ProfilePicture;

public interface ProfilePicturedao {
void saveOrUpdateProfilePicture(ProfilePicture profilePicture);
ProfilePicture  getProfilePicture(String email);
}