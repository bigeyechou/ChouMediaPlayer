package com.chou.android.choumediaplayer.datas;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import java.util.List;

/**
 * @author : zgz
 * @time :  2018/6/22 0022 11:20
 * @describe :
 **/
public class ShowVideoListBean {

    private int has_more;
    private List<ListBean> list;


    public int getHas_more() { return has_more;}


    public void setHas_more(int has_more) { this.has_more = has_more;}


    public List<ListBean> getList() { return list;}


    public void setList(List<ListBean> list) { this.list = list;}


    public static class ListBean implements MultiItemEntity {
        public static final int VIDEO_LAND = 1;
        public static final int VIDEO_VERTICAL = 0;
        private String id;
        private String video_href;
        private String video_long_time;
        private String user_id;
        private String is_boutique;
        private String music_id;
        private String video_title;
        private String video_cover;
        private String video_introduction;
        private int video_type;
        private String video_like_nums;
        private String video_play_nums;
        private String video_collect_nums;
        private String video_upload_time;
        private int is_like_video;
        private String share_url;
        private String dance_name = "街舞";
        private UserBean user;


        public String getDance_name() {
            return dance_name;
        }


        public void setDance_name(String dance_name) {
            this.dance_name = dance_name;
        }


        public String getId() { return id;}


        public void setId(String id) { this.id = id;}


        public String getVideo_href() { return video_href;}


        public void setVideo_href(String video_href) { this.video_href = video_href;}


        public String getVideo_long_time() { return video_long_time;}


        public void setVideo_long_time(String video_long_time) {
            this.video_long_time = video_long_time;
        }


        public String getUser_id() { return user_id;}


        public void setUser_id(String user_id) { this.user_id = user_id;}


        public String getIs_boutique() { return is_boutique;}


        public void setIs_boutique(String is_boutique) { this.is_boutique = is_boutique;}


        public String getMusic_id() { return music_id;}


        public void setMusic_id(String music_id) { this.music_id = music_id;}


        public String getVideo_title() { return video_title;}


        public void setVideo_title(String video_title) { this.video_title = video_title;}


        public String getVideo_cover() { return video_cover;}


        public void setVideo_cover(String video_cover) { this.video_cover = video_cover;}


        public String getVideo_introduction() { return video_introduction;}


        public void setVideo_introduction(String video_introduction) {
            this.video_introduction = video_introduction;
        }


        public int getVideo_type() { return video_type;}


        public void setVideo_type(int video_type) { this.video_type = video_type;}


        public String getVideo_like_nums() { return video_like_nums;}


        public void setVideo_like_nums(String video_like_nums) {
            this.video_like_nums = video_like_nums;
        }


        public String getVideo_play_nums() { return video_play_nums;}


        public void setVideo_play_nums(String video_play_nums) {
            this.video_play_nums = video_play_nums;
        }


        public String getVideo_collect_nums() { return video_collect_nums;}


        public void setVideo_collect_nums(String video_collect_nums) {
            this.video_collect_nums = video_collect_nums;
        }


        public String getVideo_upload_time() { return video_upload_time;}


        public void setVideo_upload_time(String video_upload_time) {
            this.video_upload_time = video_upload_time;
        }


        public int getIs_like_video() { return is_like_video;}


        public void setIs_like_video(int is_like_video) { this.is_like_video = is_like_video;}


        public String getShare_url() { return share_url;}


        public void setShare_url(String share_url) { this.share_url = share_url;}


        public UserBean getUser() { return user;}


        public void setUser(UserBean user) { this.user = user;}


        @Override public int getItemType() {
            return video_type;
        }


        public static class UserBean {

            private String id;
            private String icon;
            private String username;


            public String getId() { return id;}


            public void setId(String id) { this.id = id;}


            public String getIcon() { return icon;}


            public void setIcon(String icon) { this.icon = icon;}


            public String getUsername() { return username;}


            public void setUsername(String username) { this.username = username;}
        }
    }
}
