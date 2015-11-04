package com.myapplication.punchlineprep;

/**
 * Created by Jit on 10/26/2015.
 *
 */
public class JokeClass {

    int _id;
    String _title;
    String _upvotes;
    String _downvotes;
    String _length;
    String _timestamp;
    String _voted;

    public JokeClass(){}

    public JokeClass(int id, String title, String upvotes, String downvotes, String length, String timestamp, String voted){
        this._id = id;
        this._title = title;
        this._upvotes = upvotes;
        this._downvotes = downvotes;
        this._length = length;
        this._timestamp = timestamp;
        this._voted = voted;
    }

    public JokeClass(String title, String upvotes, String downvotes, String length, String timestamp, String voted){
        this._title = title;
        this._upvotes = upvotes;
        this._downvotes = downvotes;
        this._length = length;
        this._timestamp = timestamp;
        this._voted = voted;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int i){
        this._id = i;
    }

    public String getTitle(){
        return this._title;
    }

    public void setTitle(String t){
        this._title = t;
    }

    public String getUpvotes(){
        return this._upvotes;
    }

    public void setUpvotes(String u){
        this._upvotes = u;
    }

    public String getDownvotes(){
        return this._downvotes;
    }

    public void setDownvotes(String d){
        this._downvotes = d;
    }

    public String getLength() {return  this._length;}

    public void setLength(String l) {this._length = l;}

    public String getTimestamp(){return  this._timestamp;}

    public void setTimestamp(String t) {this._timestamp = t;}

    public String getVoted() {return this._voted;}

    public void setVoted(String tf) {this._voted = tf;}

}
