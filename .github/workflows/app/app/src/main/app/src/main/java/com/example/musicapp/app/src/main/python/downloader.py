import yt_dlp
import os

def download(url):
    try:
        output_path = "/storage/emulated/0/Download/%(title)s.%(ext)s"
        
        ydl_opts = {
            'format': 'bestaudio/best',
            'outtmpl': output_path,
            'quiet': True,
            'postprocessors': [{
                'key': 'FFmpegExtractAudio',
                'preferredcodec': 'mp3',
                'preferredquality': '320',
            }],
        }
        
        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            info = ydl.extract_info(url, download=True)
            return f"下载完成: {info['title']}"
            
    except Exception as e:
        return f"失败: {str(e)}"
