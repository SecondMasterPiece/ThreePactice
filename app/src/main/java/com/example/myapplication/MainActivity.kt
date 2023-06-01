package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MainActivity : AppCompatActivity() {
    private lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(MyRenderer(this))
        setContentView(glSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }
}

class CharacterModel(private val context: Context) {
    private var vertices: FloatArray? = null
    private var vertexCount = 0
    private val modelMatrix = FloatArray(16)

    init {
        loadModelFromResource()
    }

    private fun loadModelFromResource() {


        val assetManager = context.assets
        val inputStream = assetManager.open("PolyCat.obj")
// inputStream을 사용하여 파일 데이터를 읽을 수 있습니다.
// 필요한 파일 처리 작업을 수행합니다.
        inputStream.close() // 파일 사용이 끝난 후에는 반드시 닫아주어야 합니다.`
        val reader = BufferedReader(InputStreamReader(inputStream))

        val vertexList = mutableListOf<Float>()

        reader.useLines { lines ->
            lines.forEach { line ->
                val tokens = line.split(" ")
                if (tokens.isNotEmpty()) {
                    when (tokens[0]) {
                        "v" -> {
                            val x = tokens[1].toFloat()
                            val y = tokens[2].toFloat()
                            val z = tokens[3].toFloat()
                            vertexList.add(x)
                            vertexList.add(y)
                            vertexList.add(z)
                        }
                        // 다른 정보가 필요한 경우 처리합니다.
                    }
                }
            }
        }

        // 정점 데이터를 FloatArray로 변환합니다.
        vertices = vertexList.toFloatArray()
        vertexCount = vertices!!.size / 3
    }

    fun draw(projectionMatrix: FloatArray) {
        // 모델 행렬 설정
        Matrix.setIdentityM(modelMatrix, 0)
        // 원하는 변환 및 위치 조정

        // 버텍스 버퍼를 생성하고 데이터를 전달합니다.
        val vertexBuffer = ByteBuffer.allocateDirect(vertices!!.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertices)
        vertexBuffer.position(0)

        // 셰이더 프로그램 사용 등의 그리기 작업을 수행합니다.
        // 여기에는 OpenGL ES 호출이 포함됩니다.
        // 이 부분은 앱의 그래픽 엔진에 따라 다를 수 있습니다.
        // OpenGL 또는 다른 그래픽 라이브러리를 사용하여 구현할 수 있습니다.
    }
}

class MyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var characterModel: CharacterModel

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        characterModel = CharacterModel(context)
    }
    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 투영 행렬 생성 (원하는 변환 및 위치 조정)
        val projectionMatrix = FloatArray(16)
        // Matrix.frustumM(), Matrix.orthoM() 등의 메서드를 사용하여 투영 행렬을 생성합니다.
        // 필요한 변환과 위치 조정을 수행하세요.

        characterModel.draw(projectionMatrix)
    }
    private var previousTime: Long = 0
    private val animationDuration = 1000 // 애니메이션의 전체 시간 (밀리초)
}