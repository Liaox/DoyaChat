package com.example.EmotionChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatItem> {
    public ChatAdapter(Context context, List<ChatItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater
                = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ChatItem item = getItem(position);
        if (item.getOwnerId() == DoyaPreferences.getMyId(getContext())) {
            convertView = inflater.inflate(R.layout.chat_item_right, null);
        } else {
            convertView = inflater.inflate(R.layout.chat_item_left, null);
        }
        ((TextView) convertView.findViewById(R.id.chat_content)).setText(item.getContent());
        return convertView;
    }
}
