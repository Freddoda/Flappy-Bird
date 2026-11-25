const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

class Pipe{
    constructor(width, height, speed){
        this.width = width;
        this.height = height;
        this.speed = speed;
    }
}

class Bird{
    constructor(radius,gHeight){
        this.radius=radius;
        this.jtimer=0;
        this.height=(940-gHeight)/2;
    }

    update(canvas){

    }

    draw(drawing){
        drawing.fillstyle = 'yellow';
        drawing.beginPath();
        drawing.arc(200,this.height,this.radius,0, Math.PI * 2, false);
        drawing.fill();
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
        this.Pipes = [new Pipe(pWidth, 0, pSpeed), new Pipe(pWidth, 0, pSpeed)];
    }

    draw(canvas){
        this.bird.draw(canvas);
    }

    update(canvas){

    }

    async run(canvas){
        let playing = false;
        let startable = false;
        let milsecWait = 16;
        let drawing = canvas.getContext('2d');

        while (true){
            let start = Date.now();
            drawing.clearRect(0,0,canvas.width,canvas.height);
        
            this.update(canvas);
            this.draw(drawing);

            await wait(milsecWait-(Date.now()-start));
        }
    }
}