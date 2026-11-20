class Pipe{
    constructor(width, height, speed){
        this.width = width;
        this.height = height;
        this.speed = speed;
    }
}

class Bird{
    constructor(radius){
        this.radius=radius;
    }
}

class Ground{
    constructor(height){
        this.height=height;
    }
}

class Game {
    constructor(pWdith, pSpeed, bRad, gHeight){
        this.bird = new Bird(bRad);
        this.ground = new Ground(gHeight);
        this.Pipes = [new Pipe(0, 0, 0), new Pipe(0, 0, 0)]
    }

    run(canvas){

    }
}