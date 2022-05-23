package com.developersboard.constant.user;

import com.developersboard.constant.ErrorConstants;

/**
 * Profile constant provides details about user profile.
 *
 * @author Eric Opoku
 */
public final class ProfileConstants {

  /** URL Mapping Constants. */
  public static final String PROFILE_MAPPING = "/profile";

  public static final String PROFILE_UPDATE_MAPPING = "/update";

  /** URL Redirect Mapping Constants. */
  public static final String REDIRECT_TO_PROFILE = "redirect:/profile";

  /** View Name Constants. */
  public static final String USER_PROFILE_VIEW_NAME = "user/profile";

  /** Profile Model Keys. */
  public static final String NEW_PROFILE = "newProfile";

  public static final String DEFAULT = "default";
  public static final String UPDATE_MODE = "updateMode";
  public static final String UPDATE_SUCCESS = "updateSuccess";
  public static final String USER_HISTORIES = "userHistories";
  public static final String PIC_SUM_PHOTOS_150_RANDOM = "https://picsum.photos/150/150/?random";

  private ProfileConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
