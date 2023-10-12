package com.smilearts.smilenotes.util

import android.net.Uri
import android.util.Log
import com.smilearts.smilenotes.repository.RepositoryUtil
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

class DataStorage {

    private val TAG = "DataStorage"
    private var FILE_PATH = ""

    fun storeData(name: String ,data: String, path: String): Int {
        FILE_PATH = path
        try {
            if (getStorageDir(name) == null) {
                val fileName = File("$FILE_PATH/$name")
                val writer = BufferedWriter(FileWriter(fileName))
                writer.write(data)
                writer.close()
                Log.d(TAG, "Complete Create Local Storage")
                0
            } else {
                val fileName = File("$FILE_PATH/$name")
                val writer = BufferedWriter(FileWriter(fileName))
                writer.write(data)
                writer.close()
                Log.d(TAG, "Complete Create Local Storage")
                0
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ${e.localizedMessage}")
            return -1
        }
        return 0
    }

    fun readFromFile(util: RepositoryUtil, path: String): Int? {
        var ret = ""
        try {
            val inputStream: InputStream = FileInputStream(File(path))
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                val stringBuilder = StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append(receiveString)
                }
                inputStream.close()
                ret = stringBuilder.toString()
                if (ret != null) {
                    util.notesRep.restoreBackup(ret)
                    Log.d(TAG , "Read Success")
                    return 0
                }
            } else {
                Log.d(TAG , "Read failed")
                return -1
            }
            return 0
        } catch (e: FileNotFoundException) {
            Log.e("FileToJson", "File not found: $e")
            return -1
        } catch (e: IOException) {
            Log.e("FileToJson", "Can not read file: $e")
            return -1
        }
    }

    private fun getStorageDir(fileName: String): String? {
        val file = File(FILE_PATH)
        if (!file.exists()) {
            file.mkdirs()
            Log.d(TAG , "Folder Created Successful : ${file.absolutePath + File.separator.toString() + fileName}")
        } else {
            return null
        }
        return file.absolutePath + File.separator.toString() + fileName
    }

    fun getPathURI() : Uri {
        return Uri.fromFile(File(FILE_PATH))
    }

    fun getFileList() : List<String> {
        var fileList: ArrayList<String> = ArrayList()
        val file = File(FILE_PATH)
        val files: Array<File> = file.listFiles()
        if (files != null) {
            for (element in files) {
                fileList.add(element.name)
            }
        }
        return fileList
     }

}