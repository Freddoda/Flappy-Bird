const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

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
        this.jtimer=0;
    }
}

class Ground{
    constructor(height){
        this.height=height;
    }
}

class Game {
    constructor(pWidth, pSpeed, bRad, gHeight){
        this.bird = new Bird(bRad);
        this.ground = new Ground(gHeight);
        this.Pipes = [new Pipe(pWidth, 0, pSpeed), new Pipe(pWidth, 0, pSpeed)]
    }

    draw(canvas){

    }

    update(canvas){

    }

    async run(canvas){
        let playing = false;
        let startable = false;
        let milsecWait = 16;

        while (true){
            let start = Date.now();
        
            this.update(canvas);
            this.draw(canvas);

            await wait(milsecWait-(Date.now()-start));
        }
    }
}