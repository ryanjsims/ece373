function getVertSource(){
  return `#version 300 es
  layout (location = 0) in vec3 aPos;
  layout (location = 1) in vec3 aNormal;
  layout (location = 2) in vec2 aTexCoord;

  uniform mat4 model;
  uniform mat4 normalModel;
  uniform mat4 view;
  uniform mat4 projection;

  out vec3 normal;
  out vec3 fragPos;

  //out vec3 ourColor;
  out vec2 texCoords;

  void main() {
      fragPos = vec3(model * vec4(aPos, 1.0));
      gl_Position = projection * view * model * vec4(aPos, 1.0);
      normal = mat3(normalModel) * aNormal;
      //ourColor = aColor;
      texCoords = aTexCoord;
  }
  `;
}

function getToySource(){
  return `#version 300 es
  precision highp float;
  out vec4 FragColor;

  struct Material {
      sampler2D diffuse;
      sampler2D specular;
      float     shininess;
  };

  struct Light {
      vec3 position;

      vec3 ambient;
      vec3 diffuse;
      vec3 specular;
  };

  in vec3 normal;
  in vec3 fragPos;
  in vec2 texCoords;

  uniform Material material;
  uniform Light light;
  uniform vec3 viewPos;

  void main() {
      //Ambient
      vec3 ambient = light.ambient * vec3(texture(material.diffuse, texCoords));

      vec3 norm = normalize(normal);
      vec3 lightDir = normalize(light.position - fragPos);

      float diff = max(dot(norm, lightDir), 0.0);
      vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, texCoords));

      vec3 viewDir = normalize(viewPos - fragPos);
      vec3 reflectDir = reflect(-lightDir, norm);

      float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
      vec3 specular = vec3(texture(material.specular, texCoords)) * spec * light.specular;

      vec3 result = (ambient + diffuse + specular) * vec3(texture(material.diffuse, texCoords));

      FragColor = vec4(result, 1.0f);
  }
  `;
}

function getLightSource(){
  return `#version 300 es

  precision highp float;
  out vec4 FragColor;

  void main() {
      FragColor = vec4(1.0);
  }
  `;
}

function initShaders(gl, vertSource, fragSource){
  const vert = loadShader(gl, gl.VERTEX_SHADER, vertSource);
  const frag = loadShader(gl, gl.FRAGMENT_SHADER, fragSource);

  const program = gl.createProgram();
  gl.attachShader(program, vert);
  gl.attachShader(program, frag);
  gl.linkProgram(program);

  if(!gl.getProgramParameter(program, gl.LINK_STATUS)){
    alert("Unable to init shader program: " + gl.getProgramInfoLog(program));
    return null;
  }

  return program;
}

function loadShader(gl, type, source){
  const shader = gl.createShader(type);

  gl.shaderSource(shader, source);

  gl.compileShader(shader);

  if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
    alert('An error occurred compiling the shaders: ' + gl.getShaderInfoLog(shader));
    gl.deleteShader(shader);
    return null;
  }

  return shader;
}
