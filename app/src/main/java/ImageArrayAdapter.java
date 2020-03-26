import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dblgroup14.app.R;
import java.util.List;

public class ImageArrayAdapter extends ArrayAdapter<Integer> {
        
        
        public ImageArrayAdapter(Context context, List<Integer> images) {
            super(context, 0, images);
        }
    
   
    
    @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ImageView imageView;
            if (convertView == null)
            {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(250,250));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(6, 6, 6, 6);
            }
            else
            {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(getItem(position));
            return imageView;
        }
    }
    

