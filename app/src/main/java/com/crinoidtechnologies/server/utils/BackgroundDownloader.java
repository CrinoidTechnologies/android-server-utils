package com.crinoidtechnologies.server.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.crinoidtechnologies.server.models.ServerRequest;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by shubham on 10/17/2016.
 */

public class BackgroundDownloader extends AsyncTask<BackgroundDownloader.DownloadData, Integer, String> {

    DownloadData data = null;

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(DownloadData... params) {
        int count;
        data = params[0];
        String result = "error";
        try {
            String urlString = params[0].request.getUrl();
            String filePath = params[0].downloadFilePath;

            Log.d("asdf", "downloading file " + urlString + " \n to path " + filePath);
            URL url = new URL(urlString);

            URLConnection conexion = url.openConnection();

            for (Object key : params[0].request.getHeaders().keySet()) {
                conexion.setRequestProperty((String) key, (String) params[0].request.getHeaders().get(key));
            }

            conexion.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int fileLength = conexion.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(conexion.getInputStream());
            OutputStream output = new FileOutputStream(filePath);

            byte data[] = new byte[4096];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                //Log.d("asdfprogress",".. "+total+" .. "+fileLength);
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            Log.d("asdf", "file downloaded");
            result = "complete";

        } catch (FileNotFoundException e) {
            //onError(params[0]);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            //onError(params[0]);
            e.printStackTrace();
        } catch (IOException e) {
            //onError(params[0]);
            //e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s.equals("error"))
            onError(data);
        else if (s.equals("complete")) {
            if (data.downloadResponse != null) {
                data.downloadResponse.onDownloaded(data);
            }
        }
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (data != null && data.downloadResponse != null) {
            int percent = values[0];
            int bytes = 0;
            if (percent < 0)
                bytes = Math.abs(percent);

            data.downloadResponse.onProgress(values[0], bytes, data);
        }
    }

    void onError(DownloadData response) {
        if (response != null && response.downloadResponse != null)
            response.downloadResponse.onError(null);
    }

    public interface DownloadResponse {
        void onDownloaded(DownloadData data);

        void onError(DownloadData data);

        void onProgress(int percent, int bytes, BackgroundDownloader.DownloadData data);
    }

    public static class DownloadData {
        ServerRequest request;
        String downloadFilePath;
        DownloadResponse downloadResponse;

        public DownloadData(ServerRequest request, String downloadFilePath, DownloadResponse response) {
            this.request = request;
            this.downloadFilePath = downloadFilePath;
            this.downloadResponse = response;
        }

        public ServerRequest getRequest() {
            return request;
        }

        public void setRequest(ServerRequest request) {
            this.request = request;
        }

        public String getDownloadFilePath() {
            return downloadFilePath;
        }

        public void setDownloadFilePath(String downloadFilePath) {
            this.downloadFilePath = downloadFilePath;
        }

        public DownloadResponse getDownloadResponse() {
            return downloadResponse;
        }

        public void setDownloadResponse(DownloadResponse downloadResponse) {
            this.downloadResponse = downloadResponse;
        }
    }

}
