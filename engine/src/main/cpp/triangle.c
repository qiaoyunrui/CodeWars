//
// Created by Juhezi on 2018/2/1.
//

#include <GLES3/gl3.h>
#include <stdlib.h>
#include <EGL/egl.h>

typedef struct {
    //Handle to a program object
    GLuint programObject;
} UserData;

typedef struct {

    /// Put platform specific data here
    void *platformData;

    /// Put your user data here...
    void *userData;

    /// Window width
    GLint width;

    /// Window height
    GLint height;

} ESContext;

/**
 * Create a shader object, load the shader source, and
 * compile the shader.
 * @param type
 * @param shaderSrc
 * @return
 */
GLuint LoadShader(GLenum type, const char *shaderSrc) {
    GLuint shader;
    GLint compiled;

    //Create the shader object
    shader = glCreateShader(type);
    if (shader == 0)
        return 0;

    //Load the shader source
    glShaderSource(shader, 1, &shaderSrc, NULL);

    //Compile the Shader
    glCompileShader(shader);

    //Check the compile status
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);

    if (!compiled) {
        GLint infoLen = 0;
        glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
        if (infoLen > 1) {
            char *infoLog = malloc(sizeof(char) * infoLen);
            glGetShaderInfoLog(shader, infoLen, NULL, infoLog);
            printf("Error compiling shader:\n%s\n", infoLog);
            free(infoLog);
        }
        glDeleteShader(shader);
        return 0;
    }
    return shader;
}

/**
 * Initialize the shader and program object.
 *
 * @param esContext
 * @return
 */
int Init(ESContext *esContext) {
    UserData *userData = esContext->userData;
    char vShaderStr[] =
            "#version 300 es                          \n"
                    "layout(location = 0) in vec4 vPosition;  \n"
                    "void main()                              \n"
                    "{                                        \n"
                    "   gl_Position = vPosition;              \n"
                    "}                                        \n";

    char fShaderStr[] =
            "#version 300 es                              \n"
                    "precision mediump float;                     \n"
                    "out vec4 fragColor;                          \n"
                    "void main()                                  \n"
                    "{                                            \n"
                    "   fragColor = vec4 ( 1.0, 0.0, 0.0, 1.0 );  \n"
                    "}                                            \n";

    GLuint vertexShader;
    GLuint fragmentShader;
    GLuint programObject;
    GLint linked;

    //Load the vertex/fragment shaders
    vertexShader = LoadShader(GL_VERTEX_SHADER, vShaderStr);
    fragmentShader = LoadShader(GL_FRAGMENT_SHADER, fShaderStr);

    //Create the program object
    programObject = glCreateProgram();

    if (programObject == 0)
        return 0;

    glAttachShader(programObject, vertexShader);
    glAttachShader(programObject, fragmentShader);

    //Link the program
    glLinkProgram(programObject);

    //Check the link status
    glGetProgramiv(programObject, GL_LINK_STATUS, &linked);

    if (!linked) {
        GLint infoLen = 0;

        glGetProgramiv(programObject, GL_INFO_LOG_LENGTH, &infoLen);

        if (infoLen > 1) {
            char *infoLog = malloc(sizeof(char) * infoLen);

            glGetProgramInfoLog(programObject, infoLen, NULL, infoLog);

            printf("Error linking program:\n%s\n", infoLog);

            free(infoLog);
        }
        glDeleteProgram(programObject);
        return 0;

    }
    userData->programObject = programObject;
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    return 1;
}

/**
 * Draw a triangle using the shader pair created in Init()
 * @param esContext
 */
void Draw(ESContext *esContext) {
    UserData *userData = esContext->userData;
    GLfloat vVertices[] = {
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f};

    //Set the viewport
    glViewport(0, 0, esContext->width, esContext->height);

    //Clear the color buffer
    glClear(GL_COLOR_BUFFER_BIT);

    //Use the program object
    glUseProgram(userData->programObject);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, vVertices);
    glEnableVertexAttribArray(0);

    glDrawArrays(GL_TRIANGLES, 0, 3);
}

void shutdown(ESContext *esContext) {
    UserData *userData = esContext->userData;
    glDeleteProgram(userData->programObject);
}

int esMain(ESContext *esContext) {
    esContext->userData = malloc(sizeof(UserData));
}