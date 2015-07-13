package com.yixia.camera.demo.preference;

/**
 * 
 * @author tangjun
 * 
 */
public final class PreferenceKeys {

	public static final String PERFERENCE = "perference.db";

	/** 相册缓存文件夹满了 */
	public static final String PHOTO_CACHE_FULL = "photo_cache_full";

	public static final String FLOWER_DAY = "flower_day";

	/** 录制页提示 */
	public static final String RECORD_ONLINE_TIPS = "record_online_tips";
	/** 录制页提示 */
	public static final String RECORD_TIPS = "record_tips";
	/** 首页拍摄引导 */
	public static final String RECORD_TIPS_FIRST = "record_tips_first";
	/** 高级编辑引导 */
	public static final String RECORD_TIPS_SENIOR_EDITOR = "record_tips_senior_editor";
	/** 视频画面拖动提示-左右 */
	public static final String VIDEO_EDIT_TIPS_LR = "video_edit_tips_lr";
	/** 视频画面拖动提示-上下 */
	public static final String VIDEO_EDIT_TIPS_TB = "video_edit_tips_tb";

	/** 默认主题 */
	public static final String THEME_CURRENT_DEFAULT = "default";
	/** 当前主题 */
	//	public static final String THEME_CURRENT = THEME_CURRENT_DEFAULT;
	/** 当前主题列表 */
	public static final String THEME_CURRENT_SERIES_THEMES = "_SERIES_THEMES";
	/** 主题版本 */
	public static final String THEME_CURRENT_VERSION = "theme_current_version";
	/** 默认主题更新时间 */
	public static final String THEME_DEFAULT_UPDATE_TIME = "theme_default_update_time";
	/** 默认主题排序 */
	public static final String THEME_DEFAULT_ORDER = "theme_default_order";
	/** 主题排序 */
	public static final String THEME_ORDER = "theme_order_";
	/** 作者名称 */
	public static final String THEME_LOGO_AUTHOR_NAME = "theme_logo_author_name";
	/** 作者截图宽度 */
	public static final String THEME_LOGO_AUTHOR_WIDTH = "theme_logo_author_width";
	/** 作者截图高度 */
	public static final String THEME_LOGO_AUTHOR_HEIGHT = "theme_logo_author_height";

	/** 音乐随机 */
	public static final String THEME_MUSIC_RANDOM_INDEX = "theme_music_random_index";

	/** 检测新音乐 */
	public static final String THEME_UPDATETIME = "theme_updatetime_";
	/** 检测新音乐 */
	public static final String THEME_UPDATETIME_MUSIC = "theme_updatetime_music";
	//	/** 检测MV主题 */
	//	public static final String THEME_UPDATETIME_MV = "theme_updatetime_mv";
	//	/** 检测水印 */
	//	public static final String THEME_UPDATETIME_WATERMARK = "theme_updatetime_watermark";
	//	

	/** 3G下码率 */
	public static final String VIDEO_BITRATE_3G_600K = "video_bitrate_3g_600k";
	public static final boolean VIDEO_BITRATE_3G_600K_DEFAULT = true;

	/** 拍摄时长限制 */
	public static final String VIDEO_TIME_LIMIT = "video_time_limit";
	/** 拍摄时长限制默认值 */
	public static final int VIDEO_TIME_LIMIT_DEFAULT = 10 * 1000;

	/** 是否开启上传 */
	public static final String VERSION_DEBUG = "version_debug";

	public static final String THREAD_STARTER_PREFIX_SCID = "thread_starter_prefix_";


	/** 使用系统录制 */
	public static final String USE_SYSTEM_RECORD = "use_system_record";
	public static final boolean USE_SYSTEM_RECORD_DEFAULT = false;

	/** 城市信息缓存 */
	public static final String THEME_CITY_INFO_CACHE = "theme_city_info_cache";
	/** 城市信息拼音缓存 */
	public static final String THEME_CITY_PINYIN_INFO_CACHE = "theme_city_pinyin_info_cache";
}
