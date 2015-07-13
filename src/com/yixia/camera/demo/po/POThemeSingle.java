package com.yixia.camera.demo.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.yixia.camera.demo.utils.ConvertToUtils;
import com.yixia.camera.util.FileUtils;
import com.yixia.camera.util.StringUtils;

@SuppressWarnings("serial")
public class POThemeSingle implements Serializable {
	/** 音乐（服务端定义） */
	public static final int THEME_TYPE_MUSIC = 3;
	/** 水印（服务端定义） */
	public static final int THEME_TYPE_WATERMARK = 4;
	/** 主题（服务端定义） */
	public static final int THEME_TYPE_MV = 5;
	/** 文字（服务端定义） */
	public static final int THEME_TYPE_TEXT = 6;
	/** 滤镜（服务端定义） */
	public static final int THEME_TYPE_FILTER = 11;
	/** 主题（预览页推荐的） */
	public static final int THEME_TYPE_RECOMMEND_MV = 99;
	/** 水印（预览页推荐的） */
	public static final int THEME_TYPE_RECOMMEND_WATERMARK = 98;
	/** 主题商店 */
	public static final String THEME_STONE = "Stone";
	/** 无主题 */
	public static final String THEME_EMPTY = "Empty";

	/** 主题类型 - 本地变声 */
	public static final int SOUND_TYPE_LOCAL = 1;
	/** 主题类型 - 在线变声 */
	public static final int SOUND_TYPE_ONLINE = 2;

	public transient long _id;
	/** 主题图标 */
	public String themeIcon;
	/** 主题图标（主要用于内置主题、无主题） */
	public int themeIconResource;
	/** 主题名称 */
	public String themeDisplayName;
	/** 主题文件夹名称 */
	public String themeName;
	/** 主题下载地址 */
	public String themeDownloadUrl;
	/** 主题本地地址 */
	public String themeUrl;
	/** 主题文件夹 */
	public String themeFolder;
	/** 主题更新时间 */
	public long themeUpdateAt;
	/** 是否锁定 */
	public boolean isLock;
	/** 是否需要购买 */
	public boolean isBuy;
	/** 唯一编号 */
	public String sthid;
	/** 主题角标： 0是没有，1是最新，2是最热 */
	public int pic_type;
	public String banner;
	public String channel_pic;
	/** 主题说明 */
	public String desc;
	/** 主题价格 */
	public int price;
	/** 主题预览地址 */
	public String previewVideoPath;
	/** 主题所属的二级分类，例如音乐下面的流行、摇滚 */
	public String category;
	/** 分类备份，下载时再赋值给category */
	public String categoryBackup;
	/** 检查是否是空主题 */
	public boolean isEmpty;
	/** 主题类型 */
	public int themeType;
	/**
	 * 1、用户发布一条带主题的视频 2、把秒拍APP 分享到朋友圈 3、邀请好友一次 0、是没有锁定条件的主题
	 */
	public int lockType;

	// ~~~ 音乐
	/** 音乐名称 */
	public String musicName;
	/** 主题音乐名称 */
	public String musicTitle;
	/** 主题音乐路径 */
	public String musicPath;

	// ~~~~ 下面字段用于主题内置信息
	/** 视频 */
	public String video;
	/** 音量 */
	public float volumn = -1;
	/** 变声(1无变声 2本地 3在线) */
	public int soundType;
	/** 变声参数 */
	public int soundPitch;
	/** 默认0:intp65 普通中文 1:intp65_en 普通英文 2:vivi21 方言, 一般传2即可 */
	public int soundEngine;
	/** 合成音源 0为粤语,1为台普,2为四川话,3为东北话, 4为河南话 7为蜡笔小新 */
	public int soundVoicer;
	/** 是否是MV效果主题 */
	public boolean isMv;
	/** 是否是mp4类型 */
	public boolean isMP4;
	/** 水印参数 */
	public String watermarkBlendmode;
	/** 是否是滤镜 */
	public boolean isFilter;
	/** 是否是变速 */
	public boolean isSpeed;
	public float speed;
	/** 索引 */
	public int index;

	public int position;

	/**
	 * 用于区分list中item类型
	 */
	public int itemType;

	/** 需要弹框的消息 */
	public String message;

	/** 文件后缀 */
	public String fileExt;

	// ~~~~ 文字信息

	/** 文字字体大小 */
	public int textSize;
	/** 文字粗体 */
	public boolean textBold;
	/** 文字对齐方式 */
	public String textGravity;
	/** 文字坐标X */
	public int textX;
	/** 文字坐标Y */
	public int textY;
	/** 文件背景 */
	public String textBackground;
	/** 字体颜色 */
	public String textColor;

	// ~~~~ 天气信息

	/** 是否需要获取城市信息 */
	public boolean isCity;
	/** 城市信息是否使用拼音 */
	public boolean isCityPinyin;
	/** 是否获取天气信息 */
	public boolean isWeather;

	// ~~~~ 下面字段用于下载

	/** 下载状态 */
	public int status = -1;
	/** 下载进度 */
	public int percent;

	public List<POThemeSingle> items = new ArrayList<POThemeSingle>();

	public POThemeSingle() {

	}

	/** 用于解析在线主题 */
	public POThemeSingle(JSONObject jst, int themeType) {
		sthid = jst.optString("sthid");
		themeIcon = jst.optString("icon");
		banner = jst.optString("banner");
		channel_pic = jst.optString("channel_pic");
		themeDisplayName = jst.optString("name");
		themeName = jst.optString("folder_name").replace(" ", "_");
		desc = jst.optString("desc");
		price = ConvertToUtils.toInt(jst.optString("price"));
		previewVideoPath = jst.optString("channel");
		themeDownloadUrl = jst.optString("downurl");
		themeUpdateAt = ConvertToUtils.toLong(jst.optString("update_at"));
		isLock = jst.optBoolean("is_lock");
		isBuy = jst.optBoolean("is_buy");
		pic_type = jst.optInt("pic_type");
		this.themeType = themeType;
		this.lockType = jst.optInt("lock_type");
		isMv = themeType == THEME_TYPE_RECOMMEND_MV
				|| themeType == THEME_TYPE_MV;
		if (themeType == THEME_TYPE_WATERMARK
				|| themeType == THEME_TYPE_RECOMMEND_WATERMARK) {
			isMP4 = true;
		}
	}

	/** 更新主题 */
	public void update(POThemeSingle theme) {
		if (theme == null) {
			return;
		}
		this.themeName = theme.themeName;
		this.themeDisplayName = theme.themeDisplayName;
		this.fileExt = theme.fileExt;
		this.isMv = theme.isMv;
		this.musicTitle = theme.musicTitle;
		// this.category = theme.category;
		this.isMP4 = theme.isMP4;
		this.watermarkBlendmode = theme.watermarkBlendmode;
		this.textSize = theme.textSize;
		this.textBold = theme.textBold;
		this.textGravity = theme.textGravity;
		this.textBold = theme.textBold;
		this.textGravity = theme.textGravity;
		this.textX = theme.textX;
		this.textY = theme.textY;
		this.textBackground = theme.textBackground;
		this.textColor = theme.textColor;
		this.message = theme.message;
		this.isCity = theme.isCity;
		this.isCityPinyin = theme.isCityPinyin;
		this.lockType = theme.lockType;
		if (lockType > 0) {
			isLock = true;
		}
		this.isWeather = theme.isWeather;
		this.themeFolder = theme.themeFolder;
		this.musicPath = theme.musicPath;
		this.musicTitle = theme.musicTitle;
		this.themeIcon = theme.themeIcon;
		this.isEmpty = theme.isEmpty;
	}

	/**
	 * 主题商店banner
	 * 
	 * @param jst
	 * @param bannerTheme
	 *            default null
	 */
	public POThemeSingle(JSONObject jst, String bannerTheme) {
		sthid = jst.optString("content");
		banner = jst.optString("pic");
	}

	/** 用于解析本地json */
	public POThemeSingle(JSONObject obj) {
		themeName = obj.optString("themeName").replace(" ", "_");
		themeDisplayName = StringUtils.trim(obj.optString("themeDisplayName"));
		isEmpty = obj.optBoolean("isEmpty", false);
		// 文件后缀
		fileExt = obj.optString("ext");

		// MV主题
		isMv = obj.optBoolean("isMV", false);
		if (isMv) {
			musicName = obj.optString("musicName");
			musicTitle = obj.optString("musicTitle");
			if (StringUtils.isEmpty(musicTitle)) {
				musicTitle = StringUtils.trim(musicName).replace("_", " ");
			}
			category = obj.optString("musicCategory");
		}

		// 水印
		isMP4 = obj.optBoolean("isMP4");
		if (isMP4) {
			if (StringUtils.isEmpty(fileExt)) {
				fileExt = ".mp4";
			}
			watermarkBlendmode = obj.optString("blendmode", "BlendScreen");
		}

		// 变速
		isSpeed = obj.optBoolean("isSpeed", false);
		if (isSpeed) {
			speed = (float) obj.optDouble("speed", 1F);
		}
		// 变声
		soundType = obj.optInt("soundType");
		if (isSoundEffect()) {
			soundPitch = obj.optInt("pitch");// 本地变声
			soundEngine = obj.optInt("engine");// 合成引擎
			soundVoicer = obj.optInt("voicer");// 合成音源
		}
		// 滤镜
		isFilter = obj.optBoolean("isFilter", false);
		if (isFilter && StringUtils.isEmpty(fileExt)) {
			fileExt = ".bmp";
		}

		// 文字
		JSONObject text = obj.optJSONObject("text");
		if (text != null) {
			textSize = text.optInt("textSize");
			textBold = text.optBoolean("textBold");
			textGravity = text.optString("gravity");
			textX = text.optInt("x");
			textY = text.optInt("y");
			textBackground = text.optString("background");
			textColor = text.optString("textColor");
		}
		message = obj.optString("message");// 需要转圈
		isCity = obj.optBoolean("isCity");
		isCityPinyin = obj.optBoolean("isCityPinyin");

		lockType = obj.optInt("lockType");
		if (lockType > 0) {
			isLock = true;
		}

		//
		isWeather = obj.optBoolean("isWeather");
	}

	/** 重置下载状态 */
	public void reset() {
		status = -1;
		percent = 0;
		themeUrl = "";
		isBuy = true;
	}

	/** 检测是否是内置的主题 */
	public boolean isNestMusic() {
		return StringUtils.isEmpty(themeDownloadUrl);
	}

	/** 检测是否是变声 */
	public boolean isSoundEffect() {
		return soundType > 0;
	}

	/** 是否是mv主题 */
	public boolean isMV() {
		return isMv;
	}

	/** 判断是否需要替换滤镜 */
	public boolean isFilter() {
		return isFilter;
	}

	/** 是否变速 */
	public boolean isSpeed() {
		return isSpeed;
	}

	/** 水印 */
	public boolean isWatermark() {
		return isMP4;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	/** 是否默认主题 */
	public boolean isOriginalTheme() {
		return THEME_EMPTY.equals(themeName);
	}

	/** 是否主题商店 */
	public boolean isStoneTheme() {
		return THEME_STONE.equals(themeName);
	}

	/** 获取水印路径 */
	public String getWatermarkPath() {
		return FileUtils.concatPath(themeFolder, themeName + fileExt);
	}

	/** 获取水印路径 */
	public String getFilterPath() {
		return FileUtils.concatPath(themeFolder, themeName + fileExt);
	}

	/** 是否要生成文字 */
	public boolean isText() {
		return textSize > 0;
	}

	/**
	 * 是否为免费主题
	 * 
	 * @return
	 */
	public boolean isFree() {
		if (price > 0) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("themeName:%s isMv:%s lockType:%d isCity:%s",
				themeName, isMV() ? "true" : "false", lockType, isCity ? "true"
						: "false");
	}
}
