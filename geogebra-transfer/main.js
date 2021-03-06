'use strict';

// CONFIG

const directory = '/home/lvuser/paths/';
const robotNumber = '4253';

const pathInfoFormat = {
    mode: oneOf('switch and scale', 'scale first', 'side switch'),
    stage: dependsOn('mode', mode => {
        if (mode === 'scale first') {
            return numberAnd(stage => stage >= 0 && stage <= 4);
        }
        return numberAnd(stage => stage >= 0 && stage <= 2);
    }),
    start: dependsOn(['mode', 'stage'], ([mode, stage]) => {
        if (stage === 0) {
            switch (mode) {
                case 'switch and scale':
                    return is('center');
                case 'scale first':
                    return oneOf('left', 'center', 'right');
                case 'side switch':
                    return oneOf('left', 'right');
            }
        }
        return oneOf('left', 'right');
    }),
    end: dependsOn(['mode', 'stage', 'start'], ([mode, stage, start]) => {
        if (mode === 'side switch' && (stage === 0 || stage === 1)
            || mode === 'scale first' && [1, 3, 4].includes(stage)) {
            return is(start);
        }
        if (mode === 'switch and scale' && stage === 2
            || mode === 'side switch' && stage === 2) {
            return is(oppositeOf(start));
        }
        return oneOf('left', 'right');
    }),
    direction: dependsOn('stage', stage => {
        if (stage % 2 === 0) {
            return is('forward');
        }
        return is('backward');
    })
};

function getFilename(pathInfo) {
    return `${pathInfo.mode}-${pathInfo.stage}-${pathInfo.start}-${pathInfo.end}.csv`;
}

const motorDataFormat = {
    angle: number,
    'percent difference': number
};

// END CONFIG

const clip = require('clipboardy');
const conn = require('ssh2').Client();
const SFTPStream = require('ssh2-streams').SFTPStream;

console.log('Reading data...');
const [header, bodyLines] = readData(clip.readSync());
console.log('Converting data...');
const [filename, output] = convertData(header, bodyLines);
console.log('Connecting to robot...');
upload(filename, output);

function readData(str) {
    const [headerLine, ...bodyLines] = str.trim().split('\n');
    console.assert(bodyLines.length >= 2, 'Data has less than 3 rows');
    const headerCells = headerLine.split('\t');
    console.assert(headerCells.length >= 3, 'Data has less than 3 columns');
    const emptyColumn = headerCells.indexOf('');
    console.assert(emptyColumn >= 0, 'Could not find empty column');
    console.assert(emptyColumn > 3, 'Data for robot has less than 3 columns');
    const header = headerCells.slice(2, emptyColumn).map(x => x.toLowerCase());
    checkSame(Object.keys(motorDataFormat), header, 'column');
    return [header, bodyLines];
}

function convertData(header, lines) {
    const pathInfoMap = {};
    const motorDataOutput = [];
    const columnCount = header.length + 2;
    for (let i = 0; i < lines.length; i++) {
        const [prop, value, ...motorData] =
            lines[i].split('\t', columnCount).map(x => x.toLowerCase());
        if (prop !== '') {
            console.assert(pathInfoMap[prop] === undefined, `Duplicate ${prop}`);
            console.assert(value !== '', `${prop} has no value`);
            pathInfoMap[prop] = value;
        }
        if (motorData[0] === '?' || motorData.every(x => x === '')) {
            break;
        }
        const motorDataMap = {};
        motorData.forEach((x, j) => {
            motorDataMap[header[j]] = x;
        });
        const motorDataMap1 =
            verifyFormat(motorDataFormat, motorDataMap, p => `row ${i + 2} of ${p}`);
        motorDataOutput.push(Object.keys(motorDataFormat).map(p => motorDataMap1[p]).join());
    }
    checkSame(Object.keys(pathInfoFormat), Object.keys(pathInfoMap), 'path info');
    const pathInfoMap1 = verifyFormat(pathInfoFormat, pathInfoMap);
    return [getFilename(pathInfoMap1), Object.keys(pathInfoFormat)
        .map(prop => pathInfoMap1[prop]).concat(motorDataOutput).join('\n')]
}

function checkSame(format, actual, name) {
    for (const x of format) {
        console.assert(actual.includes(x), `Could not find ${name} ${x}`);
    }
    for (const x of actual) {
        console.assert(format.includes(x), `Extra ${name} ${x}`);
    }
}

function verifyFormat(format, obj, nameFunc = name => name) {
    const result = {};
    let defer;
    do {
        defer = false;
        for (const prop in obj) {
            if (result[prop] === undefined) {
                const x = format[prop](obj[prop], nameFunc(prop), result);
                if (x === null) {
                    defer = true;
                } else {
                    result[prop] = x;
                }
            }
        }
    } while (defer);
    return result;
}

function upload(filename, data) {
    conn.on('ready', () => {
        console.log('Initializing SFTP...');
        conn.sftp((err, sftp) => {
            if (err) throw err;
            const filepath = directory + filename;
            console.log(`Opening file ${filepath}...`);
            sftp.open(filepath, 'w', (err, handle) => {
                if (err) {
                    if (err.code === SFTPStream.STATUS_CODE.NO_SUCH_FILE) {
                        console.log(`Directory ${directory} does not exist. Creating directory...`);
                        sftp.mkdir(directory, err => {
                            if (err) throw err;
                            console.log('Opening file again...');
                            sftp.open(filepath, 'w', (err, handle) => {
                                if (err) throw err;
                                writeData(handle);
                            });
                        });
                    } else {
                        throw err;
                    }
                } else {
                    writeData(handle);
                }
            });
            function writeData(handle) {
                const buffer = Buffer.from(data);
                console.log('Writing data...');
                sftp.write(handle, buffer, 0, buffer.length, 0, err => {
                    if (err) throw err;
                    console.log('Closing file...');
                    sftp.close(handle, err => {
                        if (err) throw err;
                        conn.end();
                        console.log('Successfully transferred data to robot');
                    });
                });
            }
        });
    }).connect({
        host: `roboRIO-${robotNumber}-FRC.local`,
        port: 22,
        username: 'lvuser',
        password: ''
    });
}

function oppositeOf(x) {
    switch (x) {
        case 'left':
            return 'right';
        case 'right':
            return 'left';
    }
}

function number(x, name) {
    const n = parseFloat(x);
    console.assert(!Number.isNaN(n), `${name} is not a number`);
    return n;
}

function numberAnd(pred) {
    return (x, name) => {
        const n = number(x, name);
        console.assert(pred(n), `${name} did not meet the condition:
        ${pred}`);
        return n;
    };
}

function is(a) {
    return (x, name) => {
        console.assert(x === a, `${name} = ${x} but should be ${a}`);
        return x;
    }
}

function oneOf(...xs) {
    return (x, name) => {
        console.assert(xs.includes(x), `${name} = ${x} but should be ${xs.join(' or ')}`);
        return x;
    };
}

function dependsOn(prop, f) {
    return (x, name, obj) => {
        if (Array.isArray(prop)) {
            if (prop.every(p => obj[p] !== undefined)) {
                return f(prop.map(p => obj[p]))(x, name);
            }
            return null;
        }
        if (obj[prop] !== undefined) {
            return f(obj[prop])(x, name);
        }
        return null;
    };
}
