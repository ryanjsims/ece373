import {
  glMatrix,
  mat2, mat2d, mat3, mat4,
  quat,
  vec2, vec3, vec4,
} from './gl-matrix.js';

const LMB = 1;
const RMB = 2;
const mouseSens = 0.01;

var view = mat4.create();
var projection = mat4.create();
var model = mat4.create();
var rotation = mat4.create();
var cameraPosition = vec3.fromValues(1.0, 0.0, 6.0);
var cameraTarget = vec3.fromValues(0.0, 0.0, 0.0);
var cameraUp = vec3.fromValues(0.0, 1.0, 0.0);
var vao = [];
var textureSpec = 0;
var texture = 0;
var mouseButtonsDown = 0;
var mouseLocation = [0, 0];
var clickTime = 0;

var FLOAT_SIZE = 4; //Number of bytes in a float
function main(){
  var vertices = getVertices();
  var indices = getIndices();

  const vertSource = getVertSource();
  const toySource = getToySource();
  const lightSource = getLightSource();

  const canvas = document.getElementById('glCanvas');
  const gl = canvas.getContext("webgl2");

  if(!gl){
    alert("Could not initialize WebGL2!");
    return;
  }

  canvas.onmousedown = handleMouseDown;
  document.onmousemove = handleMouseMove;
  document.onmouseup = handleMouseUp;


  gl.viewport(0, 0, 885, 885);
  gl.clearColor(0.1, 0.1, 0.1, 1.0);
  gl.clear(gl.COLOR_BUFFER_BIT);
  gl.enable(gl.DEPTH_TEST);

  view = mat4.lookAt(view,
    cameraPosition,  //pos
    cameraTarget, //target
    cameraUp   //up
  );
  projection = mat4.perspective(projection,
    Math.PI / 4,
    895 / 895,
    1.0,
    100.0
  );

  const spToy = initShaders(gl, vertSource, toySource);
  const spLight = initShaders(gl, vertSource, lightSource);
  var vertBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, vertBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);


  vao[0] = gl.createVertexArray();
  vao[1] = gl.createVertexArray();
  gl.bindVertexArray(vao[0]);
  var offset = 0;
  var stride = 8 * FLOAT_SIZE; //Number of floats per vertex * sizeof(float)
  //Shader location 0
  gl.enableVertexAttribArray(0);
  gl.vertexAttribPointer(0, 3, gl.FLOAT, false, stride, offset);
  offset = 3 * FLOAT_SIZE;
  //Shader location 1
  gl.enableVertexAttribArray(1);
  gl.vertexAttribPointer(1, 3, gl.FLOAT, false, stride, offset);
  offset = 6 * FLOAT_SIZE;
  //Shader location 2
  gl.enableVertexAttribArray(2);
  gl.vertexAttribPointer(2, 2, gl.FLOAT, false, stride, offset);

  gl.bindVertexArray(vao[1]);
  gl.bindBuffer(gl.ARRAY_BUFFER, vertBuffer);

  gl.enableVertexAttribArray(0);
  gl.vertexAttribPointer(0, 3, gl.FLOAT, false, stride, 0);

  textureSpec = gl.createTexture();
  var imageSpec = document.getElementById("spec");
  gl.bindTexture(gl.TEXTURE_2D, textureSpec);
  gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, imageSpec);
  gl.generateMipmap(gl.TEXTURE_2D);

  texture = gl.createTexture();
  var image = document.getElementById('texture');
  gl.bindTexture(gl.TEXTURE_2D, texture);
  gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);
  gl.generateMipmap(gl.TEXTURE_2D);

  renderLoop(gl, spToy, spLight);
}

function handleMouseUp(event){
  mouseButtonsDown = event.buttons;
  console.log(mouseButtonsDown);
}

function handleMouseDown(event){
  mouseButtonsDown = event.buttons;
  mouseLocation = [event.clientX, event.clientY];
  clickTime = (new Date()).getTime();
}

function handleMouseMove(){
  if(!mouseButtonsDown){
    clickTime = (new Date()).getTime();
    mouseLocation = [event.clientX, event.clientY];
    return;
  }
  if((mouseButtonsDown ^ LMB) == 0){
    var radsAboutUp = mouseSens * -(event.clientX - mouseLocation[0]);
    var radsAboutRight = mouseSens * -(event.clientY - mouseLocation[1]);
    var rotationAboutUp = mat4.create();
    mat4.rotate(rotationAboutUp, rotationAboutUp, radsAboutUp, cameraUp);

    var rotationAboutRight = mat4.create();
    var cameraRight = vec3.create();
    var cameraFront = vec3.create();
    cameraFront = vec3.subtract(cameraFront, cameraTarget, cameraPosition);
    vec3.normalize(cameraFront, cameraFront);
    vec3.cross(cameraRight, cameraFront, cameraUp);
    mat4.rotate(rotationAboutRight, rotationAboutRight, radsAboutRight, cameraRight);
    var overall = mat4.create();
    mat4.multiply(overall, rotationAboutUp, rotationAboutRight);
    mat4.multiply(rotation, rotation, overall);
    vec3.transformMat4(cameraPosition, cameraPosition, overall);
    vec3.transformMat4(cameraUp, cameraUp, rotationAboutRight);

  } else if((mouseButtonsDown ^ RMB) == 0){

  }
  clickTime = (new Date()).getTime();
  mouseLocation = [event.clientX, event.clientY];
}

function sleep(ms){
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function renderLoop(gl, spToy, spLight){
  var d = new Date();
  var t = d.getTime();
  var framerate = 60;
  var msPerFrame = Math.floor(1000 / framerate);
  var frametime = 0;
  while(true){
    draw(gl, t, spToy, spLight);
    d = new Date();
    frametime = (d.getTime() - t);
    await sleep(msPerFrame - frametime);
    d = new Date();
    t = d.getTime();
  }
}


function draw(gl, time, spToy, spLight){
  view = mat4.lookAt(view,
    cameraPosition,  //pos
    cameraTarget, //target
    cameraUp   //up
  );
  var lightPos = vec3.fromValues(0.0, 2.0, 0.0);
  var cubePos = vec3.fromValues(0.0, 0.0, 0.0);
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
  gl.useProgram(spToy);
  var tempView = mat4.create();
  //mat4.multiply(tempView, view, rotation);
  gl.uniformMatrix4fv(gl.getUniformLocation(spToy, "view"), false, view);
  gl.uniformMatrix4fv(gl.getUniformLocation(spToy, "projection"), false, projection);
  gl.activeTexture(gl.TEXTURE0);
  gl.bindTexture(gl.TEXTURE_2D, texture);
  gl.activeTexture(gl.TEXTURE1);
  gl.bindTexture(gl.TEXTURE_2D, textureSpec);
  gl.bindVertexArray(vao[0]);
  var tempModel = mat4.create();
  tempModel = mat4.translate(tempModel, model, cubePos);
  //mat4.multiply(tempModel, tempModel, rotation);
  //mat4.rotateX(model, model, (time % 5000) / 5000 * 2 * Math.PI)
  var normalModel = mat4.clone(tempModel);
  mat4.invert(normalModel, tempModel);
  mat4.transpose(normalModel, normalModel);
  gl.uniformMatrix4fv(gl.getUniformLocation(spToy, "model"), false, tempModel);
  gl.uniformMatrix4fv(gl.getUniformLocation(spToy, "normalModel"), false, normalModel);
  gl.uniform1i(gl.getUniformLocation(spToy, "material.diffuse"), 0);
  gl.uniform1i(gl.getUniformLocation(spToy, "material.specular"), 1);
  gl.uniform1f(gl.getUniformLocation(spToy, "material.shininess"), 32.0);
  gl.uniform3fv(gl.getUniformLocation(spToy, "light.ambient"), [0.5, 0.5, 0.5]);
  gl.uniform3fv(gl.getUniformLocation(spToy, "light.diffuse"), [0.7, 0.7, 0.7]);
  gl.uniform3fv(gl.getUniformLocation(spToy, "light.specular"), [1.0, 1.0, 1.0]);
  gl.uniform3fv(gl.getUniformLocation(spToy, "light.position"), lightPos);
  gl.uniform3fv(gl.getUniformLocation(spToy, "viewPos"), cameraPosition);

  gl.drawArrays(gl.TRIANGLES, 0, 36);

  mat4.identity(tempModel);
  mat4.translate(tempModel, tempModel, lightPos);
  //mat4.multiply(tempModel, tempModel, rotation);
  mat4.scale(tempModel, tempModel, [0.2, 0.2, 0.2]);

  gl.useProgram(spLight);
  gl.uniformMatrix4fv(gl.getUniformLocation(spLight, "view"), false, view);
  gl.uniformMatrix4fv(gl.getUniformLocation(spLight, "projection"), false, projection);
  gl.uniformMatrix4fv(gl.getUniformLocation(spLight, "model"), false, tempModel);

  gl.bindVertexArray(vao[1]);

  gl.drawArrays(gl.TRIANGLES, 0, 36);
  //gl.bindVertexArray(0);
}

window.onload = main;
