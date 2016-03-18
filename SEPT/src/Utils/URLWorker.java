package Utils;

import javax.swing.*;
import java.io.IOException;

/*
    Used for downloading source from a URL asynchronously.
 */
public final class URLWorker extends SwingWorker<String, Void>
{
    private String url;
    private OnTaskCompleteListener listener;

    public URLWorker(String url)
    {
        this.url = url;
    }

    protected final String doInBackground() throws IOException
    {
        return NetUtils.get(url);
    }

    protected final void done()
    {
        try
        {
            if (listener != null)
                listener.onTaskComplete(get());
        }
        catch (Exception e)
        {
            if (listener != null)
                listener.onTaskFail();
        }
    }


    public final void setOnTaskCompleteListener(OnTaskCompleteListener listener)
    {
        this.listener = listener;
    }


    public interface OnTaskCompleteListener
    {
        void onTaskComplete(String source);
        void onTaskFail();
    }
}
