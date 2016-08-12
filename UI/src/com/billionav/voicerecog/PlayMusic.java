package com.billionav.voicerecog;


import java.util.ArrayList;
import java.io.File;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.MediaStore;

public class PlayMusic implements AudioManager.OnAudioFocusChangeListener {
	private static final String TAG = "VR";
	private Context ctx;
//	private AudioManager am;

	private static final int IDX_DATA = 0;
	private static final int IDX_SONG = 2;
	private static final int IDX_ARTIST = 3;
	private static final int IDX_ALBUM = 4;
	
	private static final String SQL_OR = " or ";
	
	private static final int SEARCH_RST_SCUESS = 0;
	private static final int SEARCH_RST_ARTIST_NOT_FIND = 0x01;
	private static final int SEARCH_RST_ALBUM_NOT_FIND = 0x02;
	private static final int SEARCH_RST_GENRE_NOT_FIND = 0x04;
	private static final int SEARCH_RST_PLAY_LIST_NOT_FIND = 0x08;
	private static final int SEARCH_RST_SONG_NOT_FIND = 0x10;
	private static final int SEARCH_RST_NAME_NOT_FIND = 0x20;
	private static final int SEARCH_RST_PLAY_FAIL = 0x100;
	
	private static final int SEARCH_CND_NONE = 0;
	private static final int SEARCH_CND_ARTIST = 1;
	private static final int SEARCH_CND_SONG = 2;
	private static final int SEARCH_CND_ALBUM = 4;
	
	
	private static class SearchResult {
		public String song;
		public int rst;
	}

	public PlayMusic(Context ctx) {
		this.ctx = ctx;
	//	am = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
	}
	
	private boolean IsEmpty(String s) {
		return (null == s || s.trim().length() == 0);
	}

	private SearchResult searchSong(MusicInfo mi) {
		ContentResolver cr = ctx.getContentResolver();
		String[] proj = {
				MediaStore.Audio.Media.DATA,			//file path name
                MediaStore.Audio.Media.DISPLAY_NAME,	//song file name
                MediaStore.Audio.Media.TITLE,			//song name
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.ALBUM
		};
		
		int cnd = SEARCH_CND_NONE;
		StringBuilder sbWhere = new StringBuilder();
		if (null != mi && !mi.isEmpty()) {
			sbWhere.append(" ");
			boolean hasAnd = false;
			if (MusicInfo.isNotEmpty(mi.artist)) {
				sbWhere.append(MediaStore.Audio.Media.ARTIST + " like '%" + mi.artist + "%'");
				hasAnd = true;
				cnd |= SEARCH_CND_ARTIST;
			}
			
			if (MusicInfo.isNotEmpty(mi.song)) {
				if (hasAnd) sbWhere.append(SQL_OR);
				sbWhere.append(MediaStore.Audio.Media.TITLE + " like '%" + mi.song + "%'");
				hasAnd = true;
				cnd |= SEARCH_CND_SONG;
			}
			
			if (MusicInfo.isNotEmpty(mi.album)) {
				if (hasAnd) sbWhere.append(SQL_OR);
				sbWhere.append(MediaStore.Audio.Media.ALBUM + " like '%" + mi.album + "%'");
				hasAnd = true;
				cnd |= SEARCH_CND_ALBUM;
			}
		}
		
		VrLog.i(TAG, "Music conditon=" + sbWhere.toString());
		Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, 
				(sbWhere.length()> 0) ? sbWhere.toString() : null, null, null);
		
		ArrayList<SearchResult> rows = new ArrayList<SearchResult>();
		while (cursor.moveToNext()) {
			SearchResult r = new SearchResult();
			String s = null;
			r.song = cursor.getString(IDX_DATA);
			
			s = cursor.getString(IDX_SONG);
			if (IsEmpty(s)) {
				r.rst |= SEARCH_RST_SONG_NOT_FIND;
			}
			s = cursor.getString(IDX_ARTIST);
			if (IsEmpty(s)) {
				r.rst |= SEARCH_RST_ARTIST_NOT_FIND;
			}
			s = cursor.getString(IDX_ALBUM);
			if (IsEmpty(s)) {
				r.rst |= SEARCH_RST_ALBUM_NOT_FIND;
			}
			rows.add(r);
			if (SEARCH_CND_NONE == cnd) {
				r.rst = SEARCH_RST_SCUESS;
				break;
			}
		}
		cursor.close();
		return findBestMusic(rows, cnd);
	}
	
	private SearchResult findBestMusic(ArrayList<SearchResult> rows, int cnd) {
		SearchResult best = null;
		int bestMatch = 10000;
		for (int i = 0; i < rows.size(); ++i) {
			int m = 0;
			SearchResult cur = rows.get(i);
			if ((cur.rst & SEARCH_RST_ARTIST_NOT_FIND) != 0)
				++m;
			if ((cur.rst & SEARCH_RST_ALBUM_NOT_FIND) != 0)
				++m;
//			if ((cur.rst & SEARCH_RST_GENRE_NOT_FIND) != 0)
//				++m;
//			if ((cur.rst & SEARCH_RST_PLAY_LIST_NOT_FIND) != 0)
//				++m;
			if ((cur.rst & SEARCH_RST_SONG_NOT_FIND) != 0)
				++m;
//			if ((cur.rst & SEARCH_RST_NAME_NOT_FIND) != 0)
//				++m;
			if (m < bestMatch) {
				bestMatch = m;
				best = cur;
			}
		}
		if (null == best) {
			best = new SearchResult();
			best.rst = SEARCH_RST_ARTIST_NOT_FIND | SEARCH_RST_ALBUM_NOT_FIND 
						| SEARCH_RST_SONG_NOT_FIND;
		}
		return best;
	}
	
	public int playMusic(MusicInfo info) {
		VrLog.i(TAG, "playMusic music info=" + info);
		if (null == info) {
			return SEARCH_RST_PLAY_FAIL;
		}
		
		/*
		 * if (am.isMusicActive()) { 
		 * VrLog.i(TAG, "PlayMusic.playMusic music is active");
		 * am.abandonAudioFocus(listener); return; }
		 */
		SearchResult song = searchSong(info);
		if (null == song) {
			VrLog.e(TAG, "playMusic search result is null");
			return SEARCH_RST_PLAY_FAIL;
		}
		if (SEARCH_RST_SCUESS != song.rst) {
			VrLog.e(TAG, "playMusic not find song rst=" + song.rst);
			return song.rst;
		}
		
		File file = new File(song.song); 
		Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.setDataAndType(Uri.fromFile(file), "audio/*");
		    
		try {
			ctx.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
		VrLog.i(TAG, "playMusic startActivity ok");
		
		return SEARCH_RST_SCUESS; 
	}

	public void pauseMusic() {
		VrLog.i(TAG, "pauseMusic");
		Intent it = new Intent();
		it.setAction("com.android.music.musicservicecommand");
		it.putExtra("command", "pause");
		ctx.sendBroadcast(it);

		// am.requestAudioFocus(listener, AudioManager.STREAM_MUSIC,
		// AudioManager.AUDIOFOCUS_GAIN);
	}

	public void stopMusic() {
		VrLog.i(TAG, "stopMusic");
		Intent it = new Intent();
		it.setAction("com.android.music.musicservicecommand");
		it.putExtra("command", "stop");
		ctx.sendBroadcast(it);
	}

	public void playNext() {
		VrLog.i(TAG, "playNext");
	}

	public void playPrev() {
		VrLog.i(TAG, "playPrev");
	}
	
	@Override
	public void onAudioFocusChange(int focusChange) {
		// TODO Auto-generated method stub
	}
}
