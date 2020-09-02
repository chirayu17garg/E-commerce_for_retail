package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private int ImageNo=0;
    private ImageView InputProductImage1;
    private ImageView InputProductImage2;
    private ImageView InputProductImage3;
    private ImageView InputProductImage4;
    private ImageView InputProductImage5;
    private ImageView InputProductImage6;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri1=null;
    private Uri ImageUri2=null;
    private Uri ImageUri3=null;
    private Uri ImageUri4=null;
    private Uri ImageUri5=null;
    private Uri ImageUri6=null;

    private String productRandomKey, downloadImageUrl1=null, downloadImageUrl2=null, downloadImageUrl3=null, downloadImageUrl4=null, downloadImageUrl5=null, downloadImageUrl6=null;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage1 = (ImageView) findViewById(R.id.select_product_image1);
        InputProductImage2 = (ImageView) findViewById(R.id.select_product_image2);
        InputProductImage3 = (ImageView) findViewById(R.id.select_product_image3);
        InputProductImage4 = (ImageView) findViewById(R.id.select_product_image4);
        InputProductImage5 = (ImageView) findViewById(R.id.select_product_image5);
        InputProductImage6 = (ImageView) findViewById(R.id.select_product_image6);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);

        InputProductImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ImageNo=1;
                OpenGallery();
            }
        });

        InputProductImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ImageNo=2;
                OpenGallery();
            }
        });

        InputProductImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ImageNo=3;
                OpenGallery();
            }
        });

        InputProductImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ImageNo=4;
                OpenGallery();
            }
        });

        InputProductImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ImageNo=5;
                OpenGallery();
            }
        });

        InputProductImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ImageNo=6;
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            if(ImageNo==1) {
                ImageUri1 = data.getData();
                InputProductImage1.setImageURI(ImageUri1);
            }
            else if(ImageNo==2) {
                ImageUri2 = data.getData();
                InputProductImage2.setImageURI(ImageUri2);
            }
            else if(ImageNo==3) {
                ImageUri3 = data.getData();
                InputProductImage3.setImageURI(ImageUri3);
            }
            else if(ImageNo==4) {
                ImageUri4 = data.getData();
                InputProductImage4.setImageURI(ImageUri4);
            }
            else if(ImageNo==5) {
                ImageUri5 = data.getData();
                InputProductImage5.setImageURI(ImageUri5);
            }
            else if(ImageNo==6) {
                ImageUri6 = data.getData();
                InputProductImage6.setImageURI(ImageUri6);
            }
        }
    }

    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();


        if (ImageUri1 == null)
        {
            Toast.makeText(this, "Atleast 1 product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        if(ImageUri1!=null)
        {
            final StorageReference filePath1 = ProductImagesRef.child(ImageUri1.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask1 = filePath1.putFile(ImageUri1);

            uploadTask1.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: 1" + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Task<Uri> urlTask = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            downloadImageUrl1 = filePath1.getDownloadUrl().toString();
                            return filePath1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl1 = task.getResult().toString();
                                SaveImageURLToDatabase(downloadImageUrl1,1);
                            }
                        }
                    });
                }
            });
        }

        if(ImageUri2!=null)
        {
            final StorageReference filePath2 = ProductImagesRef.child(ImageUri2.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask2 = filePath2.putFile(ImageUri2);

            uploadTask2.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: 2" + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Task<Uri> urlTask = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            downloadImageUrl2 = filePath2.getDownloadUrl().toString();
                            return filePath2.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl2 = task.getResult().toString();
                                SaveImageURLToDatabase(downloadImageUrl2,2);
                            }
                        }
                    });
                }
            });
        }

        if(ImageUri3!=null)
        {
            final StorageReference filePath3 = ProductImagesRef.child(ImageUri3.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask3 = filePath3.putFile(ImageUri3);

            uploadTask3.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: 3" + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Task<Uri> urlTask = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            downloadImageUrl3 = filePath3.getDownloadUrl().toString();
                            return filePath3.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl3 = task.getResult().toString();
                                SaveImageURLToDatabase(downloadImageUrl3,3);
                            }
                        }
                    });
                }
            });
        }

        if(ImageUri4!=null)
        {
            final StorageReference filePath4 = ProductImagesRef.child(ImageUri4.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask4 = filePath4.putFile(ImageUri4);

            uploadTask4.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: 4" + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Task<Uri> urlTask = uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            downloadImageUrl4 = filePath4.getDownloadUrl().toString();
                            return filePath4.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl4 = task.getResult().toString();
                                SaveImageURLToDatabase(downloadImageUrl4,4);
                            }
                        }
                    });
                }
            });
        }

        if(ImageUri5!=null)
        {
            final StorageReference filePath5 = ProductImagesRef.child(ImageUri5.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask5 = filePath5.putFile(ImageUri5);
            uploadTask5.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: 5" + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Task<Uri> urlTask = uploadTask5.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            downloadImageUrl5 = filePath5.getDownloadUrl().toString();
                            return filePath5.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl5 = task.getResult().toString();
                                SaveImageURLToDatabase(downloadImageUrl5,5);
                            }
                        }
                    });
                }
            });

        }

        if(ImageUri6!=null)
        {
            final StorageReference filePath6 = ProductImagesRef.child(ImageUri6.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask6 = filePath6.putFile(ImageUri6);

            uploadTask6.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: 6" + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Task<Uri> urlTask = uploadTask6.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            downloadImageUrl6 = filePath6.getDownloadUrl().toString();
                            return filePath6.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl6 = task.getResult().toString();
                                SaveImageURLToDatabase(downloadImageUrl6,6);
                            }
                        }
                    });
                }
            });
        }

        SaveProductInfoToDatabase();
    }

    private void SaveImageURLToDatabase(String downloadImageUrl,int ino)
    {
        HashMap<String, Object> productMap = new HashMap<>();
        String key="image"+ino;
        productMap.put(key,downloadImageUrl);

        ProductsRef.child(CategoryName).child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "URL added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);

        ProductsRef.child(CategoryName).child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}