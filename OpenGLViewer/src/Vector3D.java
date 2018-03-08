public class Vector3D {
    public float x, y, z;
    public Vector3D(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a Vector3D from spherical coordinates
     * @param hAngle is the horizontal angle of the vector
     * @param vAngle is the vertical angle of the vector
     */
    public Vector3D(float hAngle, float vAngle){
        x = (float)Math.cos(vAngle) * (float)Math.sin(hAngle);
        y = (float)Math.sin(vAngle);
        z = (float)Math.cos(vAngle) * (float)Math.cos(hAngle);
    }

    public void translate(float dx, float dy, float dz){
        x += dx;
        y += dy;
        z += dz;
    }

    public void setCoords(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setCoords(float hAngle, float vAngle){
        x = (float)Math.cos(vAngle) * (float)Math.sin(hAngle);
        y = (float)Math.sin(vAngle);
        z = (float)Math.cos(vAngle) * (float)Math.cos(hAngle);
    }

    public void translate(Vector3D other){
        x += other.x;
        y += other.y;
        z += other.z;
    }

    public Vector3D add(Vector3D other){
        float x = this.x + other.x;
        float y = this.y + other.y;
        float z = this.z + other.z;
        return new Vector3D(x, y, z);
    }

    public void add(Vector3D a, Vector3D b){
        this.x = a.x + b.x;
        this.y = a.y + b.y;
        this.z = a.z + b.z;
    }

    public Vector3D sMult(float scalar){
        float x = this.x * scalar;
        float y = this.y * scalar;
        float z = this.z * scalar;
        return new Vector3D(x, y, z);
    }

    public void sMult(Vector3D other, float scalar){
        this.x = other.x * scalar;
        this.y = other.y * scalar;
        this.z = other.z * scalar;
    }

    public Vector3D cross(Vector3D other){
        float x = this.y * other.z - this.z * other.y;
        float y = this.z * other.x - this.x * other.z;
        float z = this.x * other.y - this.y * other.x;
        return new Vector3D(x, y, z);
    }

    public void cross(Vector3D a, Vector3D b){
        this.x = a.y * b.z - a.z * b.y;
        this.y = a.z * b.x - a.x * b.z;
        this.z = a.x * b.y - a.y * b.x;
    }
}
