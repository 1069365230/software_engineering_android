package com.example.taskmanager.model.database;

import android.util.Log;

import androidx.room.TypeConverter;

import com.example.taskmanager.model.taskdata.CheckListComponent;
import com.example.taskmanager.model.taskdata.CheckListTask;
import com.example.taskmanager.model.taskdata.CheckListTaskGroup;
import com.example.taskmanager.model.taskdata.attachment.AttachmentType;
import com.example.taskmanager.model.taskdata.attachment.FileAttachment;
import com.example.taskmanager.model.taskdata.attachment.SketchAttachment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converter {

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static String fromCheckListComponentList(List<CheckListComponent> checkListComponent) {
        if (checkListComponent == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CheckListComponent>>() {}.getType();
        String json = gson.toJson(checkListComponent, type);
        return json;
    }

    @TypeConverter
    public static List<CheckListComponent> toCheckListComponentList(String checkListComponentString){
        if (checkListComponentString == null) {
            return (null);
        }

        List<CheckListComponent> checkListComponentList = new ArrayList<>();
        try {
            checkListComponentList = convertedList(checkListComponentString);
        }catch (JSONException e){
            //TODO exception handling here
            Log.e("JSON", "json conversion failed");
        }
        return checkListComponentList;
    }

    private static List<CheckListComponent> convertedList(String jsonString) throws JSONException {
        //convert first the json string into json array
        JSONArray jsonArray = new JSONArray(jsonString);
        Gson gson = new Gson();

        //two types of conversion
        Type checkListType = new TypeToken<CheckListTask>() {}.getType();
        Type checkListGroupType = new TypeToken<CheckListTaskGroup>() {}.getType();

        List<CheckListComponent> checkListComponentList = new ArrayList<>();
        //iterate through the json array, depending on if the jsonObject has the key "priority" to determine whether to convert into checkList or a group from Gson
        //then put it in the checkListComponentList
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.has("priority")){
                CheckListTask checkListTask = gson.fromJson(jsonObject.toString(), checkListType);
                checkListComponentList.add(checkListTask);
            }else{
                CheckListTaskGroup checkListTaskGroup = gson.fromJson(jsonObject.toString(), checkListGroupType);
                checkListComponentList.add(checkListTaskGroup);
            }
        }

        return checkListComponentList;
    }

    @TypeConverter
    public static String fromFileAttachmentList(List<FileAttachment> fileAttachment) {
        if (fileAttachment == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FileAttachment>>() {}.getType();
        String json = gson.toJson(fileAttachment, type);
        return json;
    }

    @TypeConverter
    public static List<FileAttachment> toFileAttachmentList(String fileAttachmentString){
        if (fileAttachmentString == null) {
            return (null);
        }

        List<FileAttachment> fileAttachmentList = new ArrayList<>();
        try {
            fileAttachmentList = convertedListFileAttachment(fileAttachmentString);
        }catch (JSONException e){
            //TODO exception handling here
            Log.e("JSON", "json conversion failed");
        }
        return fileAttachmentList;
    }

    private static List<FileAttachment> convertedListFileAttachment(String jsonString) throws JSONException {
        //convert first the json string into json array
        JSONArray jsonArray = new JSONArray(jsonString);
        Gson gson = new Gson();

        //two types of conversion
        Type fileType = new TypeToken<FileAttachment>() {}.getType();
        Type sketchType = new TypeToken<SketchAttachment>() {}.getType();

        List<FileAttachment> fileAttachmentList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //the field attachmentType determines the dataType of the object
            if(jsonObject.getString("attachmentType").equals("0")){
                FileAttachment fileAttachment = gson.fromJson(jsonObject.toString(), fileType);
                fileAttachmentList.add(fileAttachment);
            }else{
                SketchAttachment sketchAttachment = gson.fromJson(jsonObject.toString(), sketchType);
                fileAttachmentList.add(sketchAttachment);
            }
        }
        return fileAttachmentList;
    }
}
