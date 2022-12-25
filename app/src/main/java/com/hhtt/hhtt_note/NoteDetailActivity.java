package com.hhtt.hhtt_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail_aactivity);

        titleEditText = findViewById(R.id.note_title_text);
        contentEditText = findViewById(R.id.note_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewbtn = findViewById(R.id.delete_btn);

        //receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!= null && !docId.isEmpty()){
            isEditMode=true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(isEditMode){
            pageTitleTextView.setText("Chỉnh sửa ghi chú");
            deleteNoteTextViewbtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener((v)->SaveNote());

        deleteNoteTextViewbtn.setOnClickListener((v)->deleteNoteFromFirebase());
    }

    void SaveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if(noteTitle == null || noteTitle.isEmpty()){
            titleEditText.setError("Tiêu đề không hợp lệ");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteFirebase(note);

    }

    void saveNoteFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //update
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //create
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note added
//                    Utility.showToast(NoteDetailActivity.this,"Ghi chú đã được thêm");
                    finish();

                }else{
                    Utility.showToast(NoteDetailActivity.this,"Lỗi trong quá trình thêm ghi chú");
                }
            }
        });
    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;

        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
//                    Utility.showToast(NoteDetailActivity.this,"Ghi chú đã được xóa");
                    finish();

                }else{
                    Utility.showToast(NoteDetailActivity.this,"Lỗi trong quá trình xóa ghi chú");
                }
            }
        });
    }
}