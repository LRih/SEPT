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


    /* called when execute is called */
    protected final String doInBackground() throws IOException
    {
        return NetUtils.get(url);
    }

    /* called when background task finishes */
    protected final void done()
    {
        // call success or fail depending on result
        try
        {
            if (listener != null)
                listener.onSuccess(get());
        }
        catch (Exception e)
        {
            if (listener != null)
                listener.onFail();
        }
    }


    public final void setOnTaskCompleteListener(OnTaskCompleteListener listener)
    {
        this.listener = listener;
    }


    public interface OnTaskCompleteListener
    {
        void onSuccess(String source);
        void onFail();
    }
}
